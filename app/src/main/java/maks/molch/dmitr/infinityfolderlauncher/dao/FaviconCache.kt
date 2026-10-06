package maks.molch.dmitr.infinityfolderlauncher.dao

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

/**
 * Downloads and caches website favicons on disk (by host).
 *
 * Strategy:
 * 1) Parse the page HTML for <link rel="icon|apple-touch-icon|…">
 * 2) Try common paths on the origin (/apple-touch-icon.png, /favicon.png, …)
 * 3) Fall back to Google / DuckDuckGo icon CDNs (accept image body even if HTTP 404)
 */
class FaviconCache(context: Context) {
    private val appContext = context.applicationContext
    private val dir = File(appContext.cacheDir, "favicons").also { it.mkdirs() }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mutex = Mutex()
    private val memory = mutableMapOf<String, Bitmap?>()
    private val inflight = mutableMapOf<String, Job>()
    private val flows = mutableMapOf<String, MutableStateFlow<Bitmap?>>()
    private val failedAt = mutableMapOf<String, Long>()

    fun bitmapFlow(url: String): StateFlow<Bitmap?> {
        val host = hostOf(url) ?: return MutableStateFlow(null).asStateFlow()
        val flow = flows.getOrPut(host) { MutableStateFlow(memory[host]) }
        ensureLoaded(host, url)
        return flow.asStateFlow()
    }

    private fun ensureLoaded(host: String, pageUrl: String) {
        memory[host]?.let {
            flows.getOrPut(host) { MutableStateFlow(it) }.value = it
            return
        }
        val cached = fileFor(host)
        if (cached.exists()) {
            scope.launch {
                val bmp = decode(cached)
                mutex.withLock {
                    memory[host] = bmp
                    flows.getOrPut(host) { MutableStateFlow(bmp) }.value = bmp
                }
            }
            return
        }
        val lastFail = failedAt[host] ?: 0L
        if (lastFail != 0L && System.currentTimeMillis() - lastFail < RETRY_MS) return

        scope.launch {
            mutex.withLock {
                if (inflight[host]?.isActive == true) return@launch
                if (memory[host] != null) return@launch
                inflight[host] = scope.launch {
                    val bmp = download(host, pageUrl)
                    mutex.withLock {
                        memory[host] = bmp
                        if (bmp != null) {
                            failedAt.remove(host)
                            runCatching {
                                fileFor(host).outputStream().use { out ->
                                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
                                }
                            }
                        } else {
                            failedAt[host] = System.currentTimeMillis()
                        }
                        flows.getOrPut(host) { MutableStateFlow(bmp) }.value = bmp
                        inflight.remove(host)
                    }
                }
            }
        }
    }

    private suspend fun download(host: String, pageUrl: String): Bitmap? = withContext(Dispatchers.IO) {
        val origin = originOf(pageUrl) ?: "https://$host"
        val normalizedPage = normalizePageUrl(pageUrl, origin)
        val fromHtml = extractIconHrefs(normalizedPage, origin)
        val candidates = LinkedHashSet<String>().apply {
            addAll(fromHtml)
            add("$origin/apple-touch-icon.png")
            add("$origin/apple-touch-icon-precomposed.png")
            add("$origin/favicon.png")
            add("$origin/favicon-32x32.png")
            add("$origin/favicon-96x96.png")
            add("$origin/badminton-service-favicon.png")
            add("$origin/favicon.ico")
            // Resolves SPA / missing favicon.ico better than Google's 16×16 placeholder.
            add("https://icon.horse/icon/$host")
            add("https://www.google.com/s2/favicons?domain_url=${Uri.encode(origin)}&sz=128")
            add("https://www.google.com/s2/favicons?domain=$host&sz=128")
            add("https://icons.duckduckgo.com/ip3/$host.ico")
        }

        for (src in candidates) {
            val bmp = fetchBitmap(src) ?: continue
            // Skip tiny CDN placeholders (often 16×16 globe on HTTP 404).
            if (bmp.width < MIN_USEFUL_PX || bmp.height < MIN_USEFUL_PX) {
                Log.d(TAG, "skip tiny ${bmp.width}x${bmp.height} from $src")
                continue
            }
            Log.i(TAG, "favicon ok $host ← $src (${bmp.width}x${bmp.height})")
            return@withContext bmp
        }
        Log.w(TAG, "favicon miss for $host page=$normalizedPage tried=${candidates.size}")
        null
    }

    private fun extractIconHrefs(pageUrl: String, origin: String): List<String> {
        val html = fetchText(pageUrl) ?: return emptyList()
        val ranked = mutableListOf<Pair<Int, String>>()
        val linkRegex = Regex(
            """<link\b[^>]*>""",
            setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL),
        )
        for (match in linkRegex.findAll(html)) {
            val tag = match.value
            val rel = attr(tag, "rel")?.lowercase() ?: continue
            if (!rel.split(Regex("""\s+""")).any {
                    it == "icon" ||
                        it == "shortcut" ||
                        it == "shortcuticon" ||
                        it.startsWith("apple-touch-icon")
                }
            ) {
                continue
            }
            val href = attr(tag, "href") ?: continue
            val abs = resolveUrl(origin, pageUrl, href) ?: continue
            val sizeScore = parseMaxSize(attr(tag, "sizes")) +
                when {
                    rel.contains("apple-touch") -> 50
                    href.contains("apple-touch", ignoreCase = true) -> 40
                    href.endsWith(".svg", ignoreCase = true) -> -20
                    href.endsWith(".ico", ignoreCase = true) -> 5
                    else -> 20
                }
            ranked += sizeScore to abs
        }
        return ranked.sortedByDescending { it.first }.map { it.second }.distinct()
    }

    private fun fetchBitmap(url: String): Bitmap? {
        val bytes = fetchBytes(url) ?: return null
        // SPA fallbacks often return HTML for missing /favicon.ico
        if (looksLikeHtml(bytes)) return null
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun fetchBytes(url: String): ByteArray? {
        return runCatching {
            val conn = open(url)
            try {
                val code = conn.responseCode
                val stream = if (code in 200..299) {
                    conn.inputStream
                } else {
                    // Google/DDG often return a PNG body with HTTP 404.
                    val type = conn.contentType.orEmpty()
                    if (type.startsWith("image/")) conn.inputStream else conn.errorStream
                } ?: return null
                stream.use { it.readBytes() }
            } finally {
                conn.disconnect()
            }
        }.getOrNull()?.takeIf { it.isNotEmpty() && it.size < MAX_BYTES }
    }

    private fun fetchText(url: String): String? {
        val bytes = fetchBytesStrictOk(url) ?: return null
        if (!looksLikeHtml(bytes) && !String(bytes).trimStart().startsWith("<")) {
            // Still try — some servers omit content-type.
        }
        return runCatching { bytes.toString(Charsets.UTF_8) }.getOrNull()
            ?.take(MAX_HTML_CHARS)
    }

    private fun fetchBytesStrictOk(url: String): ByteArray? {
        return runCatching {
            val conn = open(url)
            try {
                if (conn.responseCode !in 200..299) return null
                conn.inputStream.use { it.readBytes() }
            } finally {
                conn.disconnect()
            }
        }.getOrNull()?.takeIf { it.isNotEmpty() && it.size < MAX_BYTES }
    }

    private fun open(url: String): HttpURLConnection =
        (URL(url).openConnection() as HttpURLConnection).apply {
            connectTimeout = 10_000
            readTimeout = 10_000
            instanceFollowRedirects = true
            requestMethod = "GET"
            setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 Chrome/120.0.0.0 Mobile Safari/537.36",
            )
            setRequestProperty("Accept", "image/avif,image/webp,image/apng,image/*,*/*;q=0.8")
        }

    private fun decode(file: File): Bitmap? =
        runCatching { BitmapFactory.decodeFile(file.absolutePath) }.getOrNull()

    private fun fileFor(host: String): File {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(host.toByteArray())
            .joinToString("") { "%02x".format(it) }
            .take(32)
        return File(dir, "$digest.png")
    }

    companion object {
        private const val TAG = "IFL_Favicon"
        private const val MIN_USEFUL_PX = 32
        private const val MAX_BYTES = 1_500_000
        private const val MAX_HTML_CHARS = 400_000
        private const val RETRY_MS = 60_000L

        fun hostOf(url: String): String? = runCatching {
            val withScheme = if (url.contains("://")) url else "https://$url"
            Uri.parse(withScheme).host?.lowercase()?.trim('.')?.takeIf { it.isNotEmpty() }
        }.getOrNull()

        fun originOf(url: String): String? = runCatching {
            val withScheme = if (url.contains("://")) url else "https://$url"
            val uri = Uri.parse(withScheme)
            val scheme = uri.scheme ?: "https"
            val host = uri.host ?: return null
            val port = uri.port
            if (port != -1) "$scheme://$host:$port" else "$scheme://$host"
        }.getOrNull()

        private fun normalizePageUrl(url: String, origin: String): String {
            val withScheme = if (url.contains("://")) url else "https://$url"
            return withScheme.ifBlank { origin }
        }

        private fun attr(tag: String, name: String): String? {
            val patterns = listOf(
                Regex("""\b$name\s*=\s*"([^"]*)"""", RegexOption.IGNORE_CASE),
                Regex("""\b$name\s*=\s*'([^']*)'""", RegexOption.IGNORE_CASE),
            )
            for (p in patterns) {
                val m = p.find(tag) ?: continue
                return m.groupValues[1].trim().takeIf { it.isNotEmpty() }
            }
            return null
        }

        private fun resolveUrl(origin: String, pageUrl: String, href: String): String? {
            val h = href.trim()
            if (h.isEmpty() || h.startsWith("data:", ignoreCase = true)) return null
            if (h.startsWith("//")) {
                val scheme = Uri.parse(origin).scheme ?: "https"
                return "$scheme:$h"
            }
            if (h.contains("://")) return h
            if (h.startsWith("/")) return origin.trimEnd('/') + h
            val base = pageUrl.substringBeforeLast('/') + "/"
            return runCatching { URL(URL(base), h).toString() }.getOrNull()
        }

        private fun parseMaxSize(sizes: String?): Int {
            if (sizes.isNullOrBlank() || sizes.equals("any", ignoreCase = true)) return 0
            return sizes.split(Regex("""\s+""")).mapNotNull { token ->
                val parts = token.lowercase().split('x')
                if (parts.size != 2) return@mapNotNull null
                val w = parts[0].toIntOrNull() ?: return@mapNotNull null
                val h = parts[1].toIntOrNull() ?: return@mapNotNull null
                maxOf(w, h)
            }.maxOrNull() ?: 0
        }

        private fun looksLikeHtml(bytes: ByteArray): Boolean {
            val start = bytes.take(64).toByteArray().toString(Charsets.UTF_8).trimStart()
                .lowercase()
            return start.startsWith("<!doctype html") ||
                start.startsWith("<html") ||
                start.startsWith("<head") ||
                start.startsWith("<!--")
        }
    }
}
