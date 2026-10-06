package maks.molch.dmitr.infinityfolderlauncher.dao

import android.content.Context
import android.net.Uri
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Reads daily step totals from Xiaomi / HyperOS system pedometer DB
 * (`content://com.miui.providers.steps/item`) — the same store the stock launcher widget uses.
 */
object MiuiStepsReader {
    private const val TAG = "MiuiStepsReader"
    private val CONTENT_URI: Uri = Uri.parse("content://com.miui.providers.steps/item")
    private val PROJECTION = arrayOf("_begin_time", "_end_time", "_steps")

    fun isSupported(context: Context): Boolean {
        return runCatching {
            val clazz = Class.forName("miui.util.FeatureParser")
            val method = clazz.getMethod("getBoolean", String::class.java, Boolean::class.javaPrimitiveType)
            method.invoke(null, "support_steps_provider", false) as Boolean
        }.getOrElse {
            // FeatureParser missing on non-MIUI — still try querying.
            true
        } && context.packageManager.resolveContentProvider("com.miui.providers.steps", 0) != null
    }

    /**
     * Aggregate system step samples into dayKey → steps for the last [days] days.
     * Returns empty map if provider is unavailable / permission denied.
     */
    fun dailyTotals(context: Context, days: Int): Map<String, Int> {
        if (days <= 0) return emptyMap()
        val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getDefault()
        }
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.add(Calendar.DAY_OF_YEAR, -(days - 1))
        val rangeStartMs = cal.timeInMillis

        val totals = linkedMapOf<String, Int>()
        // Pre-fill zeros so UI has contiguous days.
        for (i in 0 until days) {
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_YEAR, -(days - 1 - i))
            totals[dayFormat.format(c.time)] = 0
        }

        val resolver = context.contentResolver
        // Provider stores begin/end as epoch millis (theme docs) — also try seconds if empty.
        val cursor = runCatching {
            resolver.query(
                CONTENT_URI,
                PROJECTION,
                "_begin_time >= ?",
                arrayOf(rangeStartMs.toString()),
                "_begin_time asc",
            )
        }.onFailure {
            Log.w(TAG, "query failed: ${it.message}")
        }.getOrNull()

        if (cursor == null) {
            // Retry with seconds in case OEM uses seconds.
            val cursorSec = runCatching {
                resolver.query(
                    CONTENT_URI,
                    PROJECTION,
                    "_begin_time >= ?",
                    arrayOf((rangeStartMs / 1000L).toString()),
                    "_begin_time asc",
                )
            }.getOrNull() ?: return emptyMap()
            cursorSec.use { aggregate(it, totals, dayFormat, useSeconds = true) }
            return totals
        }

        cursor.use { aggregate(it, totals, dayFormat, useSeconds = false) }
        // If millis query returned nothing meaningful, try seconds.
        if (totals.values.all { it == 0 }) {
            runCatching {
                resolver.query(
                    CONTENT_URI,
                    PROJECTION,
                    "_begin_time >= ?",
                    arrayOf((rangeStartMs / 1000L).toString()),
                    "_begin_time asc",
                )
            }.getOrNull()?.use { aggregate(it, totals, dayFormat, useSeconds = true) }
        }
        Log.i(TAG, "dailyTotals days=$days → $totals")
        return totals
    }

    private fun aggregate(
        cursor: android.database.Cursor,
        totals: MutableMap<String, Int>,
        dayFormat: SimpleDateFormat,
        useSeconds: Boolean,
    ) {
        val beginIdx = cursor.getColumnIndex("_begin_time")
        val stepsIdx = cursor.getColumnIndex("_steps")
        if (beginIdx < 0 || stepsIdx < 0) return
        while (cursor.moveToNext()) {
            val rawBegin = cursor.getLong(beginIdx)
            val steps = cursor.getInt(stepsIdx)
            if (steps <= 0) continue
            val beginMs = if (useSeconds || rawBegin < 10_000_000_000L) {
                // Heuristic: values < ~year 2286 in seconds → treat as seconds.
                if (rawBegin < 10_000_000_000L) rawBegin * 1000L else rawBegin
            } else {
                rawBegin
            }
            val key = dayFormat.format(Date(beginMs))
            totals[key] = (totals[key] ?: 0) + steps
        }
    }
}
