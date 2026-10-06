package maks.molch.dmitr.infinityfolderlauncher.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class IconLabelStyle(
    val fontSizeSp: Int = DEFAULT_FONT_SIZE_SP,
    val color: Color = DEFAULT_COLOR,
) {
    companion object {
        const val MIN_FONT_SIZE_SP = 10
        const val MAX_FONT_SIZE_SP = 18
        const val DEFAULT_FONT_SIZE_SP = 12
        val DEFAULT_COLOR: Color = Base0

        val COLOR_PRESETS: List<Color> = listOf(
            Base0,
            Base100,
            Base70,
            Base40,
            Green50,
            Red50,
            Color(0xFFFFD54F),
            Color(0xFF64B5F6),
        )
    }
}

val LocalIconLabelStyle = compositionLocalOf { IconLabelStyle() }
