package maks.molch.dmitr.infinityfolderlauncher.dao

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class DaySteps(
    val dayKey: String,
    val steps: Int,
)

/**
 * Tracks steps for "today" and history.
 *
 * On Xiaomi/HyperOS prefers the system pedometer DB (same as stock launcher widget).
 * Falls back to [Sensor.TYPE_STEP_COUNTER] elsewhere / if provider unavailable.
 */
class StepsDao(context: Context) : SensorEventListener {
    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val sensorManager =
        appContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepCounter: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    private val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val miuiSupported = MiuiStepsReader.isSupported(appContext)

    private val _stepsToday = MutableStateFlow(prefs.getInt(KEY_STEPS_TODAY, 0))
    val stepsToday: StateFlow<Int> = _stepsToday.asStateFlow()

    private val _available = MutableStateFlow(stepCounter != null || miuiSupported)
    val available: StateFlow<Boolean> = _available.asStateFlow()

    private val _permissionNeeded = MutableStateFlow(!hasActivityPermission())
    val permissionNeeded: StateFlow<Boolean> = _permissionNeeded.asStateFlow()

    private val _historyVersion = MutableStateFlow(0)
    val historyVersion: StateFlow<Int> = _historyVersion.asStateFlow()

    private var listening = false

    fun hasActivityPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return true
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACTIVITY_RECOGNITION,
        ) == PackageManager.PERMISSION_GRANTED
    }

    /** Last [days] calendar days including today, oldest → newest. */
    fun history(days: Int = HISTORY_DAYS): List<DaySteps> {
        ensureDayRollover()
        syncFromMiui(days.coerceAtLeast(HISTORY_DAYS))
        val map = readHistoryMap()
        // Prefer live today if sensor is ahead of cached history.
        map[todayKey()] = maxOf(map[todayKey()] ?: 0, _stepsToday.value)
        val result = ArrayList<DaySteps>(days)
        for (i in days - 1 downTo 0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -i)
            val key = dayFormat.format(cal.time)
            result += DaySteps(key, map[key] ?: 0)
        }
        return result
    }

    fun start() {
        ensureDayRollover()
        val permitted = hasActivityPermission()
        _permissionNeeded.value = !permitted
        _available.value = stepCounter != null || miuiSupported

        if (!permitted) {
            Log.w(TAG, "ACTIVITY_RECOGNITION not granted — sensor idle")
            stopListeningOnly()
            return
        }

        syncFromMiui(HISTORY_DAYS)

        if (miuiSupported) {
            // System DB already has today; still listen to sensor for near-realtime bumps.
            Log.i(TAG, "MIUI steps provider available")
        }

        val sensor = stepCounter
        if (sensor == null) {
            if (!miuiSupported) _available.value = false
            return
        }
        if (listening) return
        val ok = sensorManager.registerListener(
            this,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL,
        )
        listening = ok
        Log.i(TAG, "registerListener(TYPE_STEP_COUNTER)=$ok")
    }

    fun stop() {
        stopListeningOnly()
    }

    private fun stopListeningOnly() {
        if (!listening) return
        sensorManager.unregisterListener(this)
        listening = false
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_STEP_COUNTER) return
        ensureDayRollover()
        val cumulative = event.values.firstOrNull()?.toInt() ?: return
        val baseline = prefs.getInt(KEY_BASELINE, -1)
        if (baseline < 0 || cumulative < baseline) {
            // Seed baseline from current MIUI today so we don't reset to 0 over system total.
            val miuiToday = readHistoryMap()[todayKey()] ?: 0
            val seededBaseline = (cumulative - miuiToday).coerceAtLeast(0)
            prefs.edit {
                putInt(KEY_BASELINE, seededBaseline)
                putInt(KEY_STEPS_TODAY, miuiToday)
                putLong(KEY_BOOT, SystemClock.elapsedRealtime())
            }
            setTodaySteps(miuiToday)
            Log.i(TAG, "baseline seeded=$seededBaseline miuiToday=$miuiToday cum=$cumulative")
            return
        }
        val sensorToday = (cumulative - baseline).coerceAtLeast(0)
        val cached = readHistoryMap()[todayKey()] ?: 0
        setTodaySteps(maxOf(sensorToday, cached))
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private fun syncFromMiui(days: Int) {
        if (!miuiSupported || !hasActivityPermission()) return
        val totals = MiuiStepsReader.dailyTotals(appContext, days)
        if (totals.isEmpty()) return
        val map = readHistoryMap()
        var changed = false
        totals.forEach { (day, steps) ->
            val prev = map[day] ?: 0
            // Never overwrite a higher local value with a lower system sample.
            if (steps > prev) {
                map[day] = steps
                changed = true
            }
        }
        if (changed) {
            prefs.edit { putString(KEY_HISTORY, historyToJson(map)) }
            _historyVersion.value += 1
        }
        val today = todayKey()
        val todaySteps = maxOf(map[today] ?: 0, totals[today] ?: 0, _stepsToday.value)
        if (todaySteps != _stepsToday.value) {
            prefs.edit { putInt(KEY_STEPS_TODAY, todaySteps) }
            _stepsToday.value = todaySteps
        }
    }

    private fun setTodaySteps(today: Int) {
        if (today != _stepsToday.value) {
            prefs.edit { putInt(KEY_STEPS_TODAY, today) }
            _stepsToday.value = today
        }
        if (writeHistoryDay(todayKey(), today)) {
            _historyVersion.value += 1
        }
    }

    private fun ensureDayRollover() {
        val today = todayKey()
        val stored = prefs.getString(KEY_DAY, null)
        if (stored == today) return

        if (stored != null) {
            val previousSteps = prefs.getInt(KEY_STEPS_TODAY, 0)
            writeHistoryDay(stored, previousSteps)
        }

        prefs.edit {
            putString(KEY_DAY, today)
            putInt(KEY_BASELINE, -1)
            putInt(KEY_STEPS_TODAY, 0)
        }
        _stepsToday.value = 0
        pruneHistory()
        _historyVersion.value += 1
    }

    private fun todayKey(): String = dayFormat.format(Date())

    private fun writeHistoryDay(dayKey: String, steps: Int): Boolean {
        val map = readHistoryMap()
        val prev = map[dayKey] ?: 0
        if (steps <= prev && map.containsKey(dayKey)) return false
        map[dayKey] = maxOf(prev, steps)
        prefs.edit { putString(KEY_HISTORY, historyToJson(map)) }
        return true
    }

    private fun readHistoryMap(): MutableMap<String, Int> {
        val raw = prefs.getString(KEY_HISTORY, null) ?: return mutableMapOf()
        return runCatching {
            val json = JSONObject(raw)
            val out = mutableMapOf<String, Int>()
            json.keys().forEach { key ->
                out[key] = json.optInt(key, 0)
            }
            out
        }.getOrElse { mutableMapOf() }
    }

    private fun pruneHistory() {
        val map = readHistoryMap()
        if (map.size <= MAX_HISTORY_ENTRIES) return
        val keep = map.keys.sorted().takeLast(MAX_HISTORY_ENTRIES).toSet()
        val pruned = map.filterKeys { it in keep }
        prefs.edit { putString(KEY_HISTORY, historyToJson(pruned)) }
    }

    private fun historyToJson(map: Map<String, Int>): String {
        val json = JSONObject()
        map.forEach { (key, value) -> json.put(key, value) }
        return json.toString()
    }

    companion object {
        private const val TAG = "StepsDao"
        private const val PREFS = "steps_widget"
        private const val KEY_DAY = "day"
        private const val KEY_BASELINE = "baseline"
        private const val KEY_STEPS_TODAY = "steps_today"
        private const val KEY_BOOT = "boot_elapsed"
        private const val KEY_HISTORY = "history_json"
        const val HISTORY_DAYS = 7
        private const val MAX_HISTORY_ENTRIES = 30
    }
}
