package maks.molch.dmitr.infinityfolderlauncher.dao

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsDao(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val _mainColumns = MutableStateFlow(prefs.getInt(KEY_MAIN_COLUMNS, DEFAULT_COLUMNS))
    private val _searchColumns = MutableStateFlow(prefs.getInt(KEY_SEARCH_COLUMNS, DEFAULT_COLUMNS))

    val mainColumns: StateFlow<Int> = _mainColumns.asStateFlow()
    val searchColumns: StateFlow<Int> = _searchColumns.asStateFlow()

    fun setMainColumns(value: Int) {
        val columns = value.coerceIn(MIN_COLUMNS, MAX_COLUMNS)
        prefs.edit { putInt(KEY_MAIN_COLUMNS, columns) }
        _mainColumns.value = columns
    }

    fun setSearchColumns(value: Int) {
        val columns = value.coerceIn(MIN_COLUMNS, MAX_COLUMNS)
        prefs.edit { putInt(KEY_SEARCH_COLUMNS, columns) }
        _searchColumns.value = columns
    }

    companion object {
        const val MIN_COLUMNS = 2
        const val MAX_COLUMNS = 6
        const val DEFAULT_COLUMNS = 4
        private const val PREFS = "Settings"
        private const val KEY_MAIN_COLUMNS = "main_columns"
        private const val KEY_SEARCH_COLUMNS = "search_columns"
    }
}
