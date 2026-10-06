package maks.molch.dmitr.infinityfolderlauncher.dao

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import java.io.File
import java.util.UUID

/**
 * Copies gallery images into app-private storage so folder backgrounds
 * keep working after the source URI expires.
 */
class FolderBackgroundStore(context: Context) {
    private val appContext = context.applicationContext
    private val dir: File =
        File(appContext.filesDir, DIR_NAME).also { it.mkdirs() }

    fun absolutePath(fileName: String?): String? {
        if (fileName.isNullOrBlank()) return null
        val file = File(dir, fileName)
        return file.takeIf { it.isFile }?.absolutePath
    }

    fun importFromUri(uri: Uri): String? {
        return runCatching {
            val bitmap = decodeBitmap(uri) ?: error("decode failed for $uri")
            val name = "${UUID.randomUUID()}.jpg"
            val out = File(dir, name)
            out.outputStream().use { stream ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 88, stream)) {
                    error("compress failed")
                }
            }
            if (!bitmap.isRecycled) bitmap.recycle()
            if (!out.isFile || out.length() == 0L) {
                out.delete()
                error("empty output")
            }
            name
        }.onFailure { t ->
            Log.e(TAG, "importFromUri failed: $uri", t)
        }.getOrNull()
    }

    fun importFromUris(uris: List<Uri>): List<String> =
        uris.mapNotNull { importFromUri(it) }

    fun delete(fileName: String?) {
        if (fileName.isNullOrBlank()) return
        runCatching { File(dir, fileName).delete() }
    }

    fun deleteAll(fileNames: Collection<String?>) {
        fileNames.forEach { delete(it) }
    }

    private fun decodeBitmap(uri: Uri): Bitmap? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(appContext.contentResolver, uri)
            return ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                val max = maxOf(info.size.width, info.size.height).coerceAtLeast(1)
                if (max > MAX_EDGE) {
                    val sample = sampleSizeFor(info.size.width, info.size.height, MAX_EDGE)
                    decoder.setTargetSampleSize(sample)
                }
                decoder.isMutableRequired = false
            }
        }
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        appContext.contentResolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input, null, bounds)
        } ?: return null
        val opts = BitmapFactory.Options().apply {
            inSampleSize = sampleSizeFor(bounds.outWidth, bounds.outHeight, MAX_EDGE)
        }
        return appContext.contentResolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input, null, opts)
        }
    }

    companion object {
        private const val TAG = "FolderBgStore"
        private const val DIR_NAME = "folder_backgrounds"
        private const val MAX_EDGE = 2048

        val ROTATE_OPTIONS_SECONDS: List<Int> = listOf(5, 10, 15, 30, 60, 120, 300)

        const val DEFAULT_ROTATE_SECONDS = 15

        private fun sampleSizeFor(width: Int, height: Int, maxEdge: Int): Int {
            var sample = 1
            val w = width.coerceAtLeast(1)
            val h = height.coerceAtLeast(1)
            while (w / sample > maxEdge || h / sample > maxEdge) {
                sample *= 2
            }
            return sample.coerceAtLeast(1)
        }
    }
}
