package maks.molch.dmitr.infinityfolderlauncher.dao

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.FolderBackgrounds
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.IconLabelStyle

class SettingsDao(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val backgroundStore = FolderBackgroundStore(context)
    private val _mainColumns = MutableStateFlow(
        prefs.getInt(KEY_MAIN_COLUMNS, DEFAULT_COLUMNS).coerceIn(MIN_COLUMNS, MAX_COLUMNS),
    )
    private val _searchColumns = MutableStateFlow(
        prefs.getInt(KEY_SEARCH_COLUMNS, _mainColumns.value).coerceIn(MIN_COLUMNS, MAX_COLUMNS),
    )
    private val _defaultFolderBackground = MutableStateFlow(
        FolderBackgrounds.normalizeId(
            prefs.getString(KEY_DEFAULT_FOLDER_BACKGROUND, FolderBackgrounds.DEFAULT_ID),
        ),
    )
    private val _defaultFolderBackgroundImages = MutableStateFlow(loadDefaultImages())
    private val _defaultFolderBackgroundRotateSeconds = MutableStateFlow(
        prefs.getInt(
            KEY_DEFAULT_FOLDER_BACKGROUND_ROTATE,
            FolderBackgroundStore.DEFAULT_ROTATE_SECONDS,
        ).coerceIn(
            FolderBackgroundStore.ROTATE_OPTIONS_SECONDS.first(),
            FolderBackgroundStore.ROTATE_OPTIONS_SECONDS.last(),
        ),
    )
    private val _rotateBackgroundOnFolderChange = MutableStateFlow(
        prefs.getBoolean(KEY_ROTATE_BACKGROUND_ON_FOLDER_CHANGE, false),
    )
    private val _labelFontSizeSp = MutableStateFlow(
        prefs.getInt(KEY_LABEL_FONT_SIZE_SP, IconLabelStyle.DEFAULT_FONT_SIZE_SP)
            .coerceIn(IconLabelStyle.MIN_FONT_SIZE_SP, IconLabelStyle.MAX_FONT_SIZE_SP),
    )
    private val _labelColorArgb = MutableStateFlow(
        prefs.getInt(KEY_LABEL_COLOR_ARGB, IconLabelStyle.DEFAULT_COLOR.toArgb()),
    )

    val mainColumns: StateFlow<Int> = _mainColumns.asStateFlow()
    val searchColumns: StateFlow<Int> = _searchColumns.asStateFlow()
    val defaultFolderBackground: StateFlow<String> = _defaultFolderBackground.asStateFlow()
    val defaultFolderBackgroundImages: StateFlow<List<String>> =
        _defaultFolderBackgroundImages.asStateFlow()
    val defaultFolderBackgroundRotateSeconds: StateFlow<Int> =
        _defaultFolderBackgroundRotateSeconds.asStateFlow()
    val rotateBackgroundOnFolderChange: StateFlow<Boolean> =
        _rotateBackgroundOnFolderChange.asStateFlow()
    val labelFontSizeSp: StateFlow<Int> = _labelFontSizeSp.asStateFlow()
    val labelColorArgb: StateFlow<Int> = _labelColorArgb.asStateFlow()

    fun setMainColumns(value: Int) {
        val columns = value.coerceIn(MIN_COLUMNS, MAX_COLUMNS)
        prefs.edit {
            putInt(KEY_MAIN_COLUMNS, columns)
            putInt(KEY_SEARCH_COLUMNS, columns)
        }
        _mainColumns.value = columns
        _searchColumns.value = columns
    }

    fun setSearchColumns(value: Int) {
        val columns = value.coerceIn(MIN_COLUMNS, MAX_COLUMNS)
        prefs.edit { putInt(KEY_SEARCH_COLUMNS, columns) }
        _searchColumns.value = columns
    }

    fun setDefaultFolderBackground(value: String) {
        val id = FolderBackgrounds.normalizeId(value)
        val oldImages = _defaultFolderBackgroundImages.value
        prefs.edit {
            putString(KEY_DEFAULT_FOLDER_BACKGROUND, id)
            remove(KEY_DEFAULT_FOLDER_BACKGROUND_IMAGES)
            remove(KEY_DEFAULT_FOLDER_BACKGROUND_IMAGE) // legacy
        }
        _defaultFolderBackground.value = id
        _defaultFolderBackgroundImages.value = emptyList()
        backgroundStore.deleteAll(oldImages)
    }

    fun setDefaultFolderBackgroundImages(fileNames: List<String>) {
        val cleaned = fileNames.filter { backgroundStore.absolutePath(it) != null }.distinct()
        val oldImages = _defaultFolderBackgroundImages.value
        prefs.edit {
            putString(KEY_DEFAULT_FOLDER_BACKGROUND_IMAGES, cleaned.joinToString("\n"))
            remove(KEY_DEFAULT_FOLDER_BACKGROUND_IMAGE)
        }
        _defaultFolderBackgroundImages.value = cleaned
        backgroundStore.deleteAll(oldImages.filter { it !in cleaned })
    }

    fun setDefaultFolderBackgroundRotateSeconds(seconds: Int) {
        val value = FolderBackgroundStore.ROTATE_OPTIONS_SECONDS
            .minByOrNull { kotlin.math.abs(it - seconds) }
            ?: FolderBackgroundStore.DEFAULT_ROTATE_SECONDS
        prefs.edit { putInt(KEY_DEFAULT_FOLDER_BACKGROUND_ROTATE, value) }
        _defaultFolderBackgroundRotateSeconds.value = value
    }

    fun setRotateBackgroundOnFolderChange(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_ROTATE_BACKGROUND_ON_FOLDER_CHANGE, enabled) }
        _rotateBackgroundOnFolderChange.value = enabled
    }

    fun setLabelFontSizeSp(value: Int) {
        val size = value.coerceIn(
            IconLabelStyle.MIN_FONT_SIZE_SP,
            IconLabelStyle.MAX_FONT_SIZE_SP,
        )
        prefs.edit { putInt(KEY_LABEL_FONT_SIZE_SP, size) }
        _labelFontSizeSp.value = size
    }

    fun setLabelColor(color: Color) {
        val argb = color.toArgb()
        prefs.edit { putInt(KEY_LABEL_COLOR_ARGB, argb) }
        _labelColorArgb.value = argb
    }

    private fun loadDefaultImages(): List<String> {
        val multi = prefs.getString(KEY_DEFAULT_FOLDER_BACKGROUND_IMAGES, null)
            ?.lineSequence()
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.toList()
            .orEmpty()
        val legacy = prefs.getString(KEY_DEFAULT_FOLDER_BACKGROUND_IMAGE, null)
            ?.takeIf { it.isNotBlank() }
            ?.let { listOf(it) }
            .orEmpty()
        return (multi.ifEmpty { legacy })
            .filter { backgroundStore.absolutePath(it) != null }
            .distinct()
    }

    companion object {
        const val MIN_COLUMNS = 1
        const val MAX_COLUMNS = 7
        const val DEFAULT_COLUMNS = 4
        private const val PREFS = "Settings"
        private const val KEY_MAIN_COLUMNS = "main_columns"
        private const val KEY_SEARCH_COLUMNS = "search_columns"
        private const val KEY_DEFAULT_FOLDER_BACKGROUND = "default_folder_background"
        private const val KEY_DEFAULT_FOLDER_BACKGROUND_IMAGE = "default_folder_background_image"
        private const val KEY_DEFAULT_FOLDER_BACKGROUND_IMAGES = "default_folder_background_images"
        private const val KEY_DEFAULT_FOLDER_BACKGROUND_ROTATE = "default_folder_background_rotate"
        private const val KEY_ROTATE_BACKGROUND_ON_FOLDER_CHANGE =
            "rotate_background_on_folder_change"
        private const val KEY_LABEL_FONT_SIZE_SP = "label_font_size_sp"
        private const val KEY_LABEL_COLOR_ARGB = "label_color_argb"
    }
}
