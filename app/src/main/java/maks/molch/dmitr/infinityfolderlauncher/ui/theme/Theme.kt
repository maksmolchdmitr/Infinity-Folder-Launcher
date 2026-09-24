package maks.molch.dmitr.infinityfolderlauncher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = lightColorScheme(
    primary = Green50,
    secondary = Orange20,
    tertiary = Base70,
    background = Base5,
    surface = Base0,
    onPrimary = Base0,
    onSecondary = Base100,
    onBackground = Base100,
    onSurface = Base100,
)

@Composable
fun InfinityFolderLauncherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content,
    )
}

val WallpaperColor = Color(0xFFE8F5EF)

val WallpaperGradientColors = listOf(
    Color(0xFF5B4B8A),
    Color(0xFF2A9D8F),
    Color(0xFFE76F9B),
)
