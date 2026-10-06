package maks.molch.dmitr.infinityfolderlauncher.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

data class FolderBackgroundPreset(
    val id: String,
    val colors: List<Color>,
)

object FolderBackgrounds {
    const val DEFAULT_ID = "aurora"

    val presets: List<FolderBackgroundPreset> = listOf(
        FolderBackgroundPreset(
            id = DEFAULT_ID,
            colors = listOf(
                Color(0xFF5B4B8A),
                Color(0xFF2A9D8F),
                Color(0xFFE76F9B),
            ),
        ),
        FolderBackgroundPreset(
            id = "ocean",
            colors = listOf(
                Color(0xFF1B3A4B),
                Color(0xFF2A9D8F),
                Color(0xFF90E0EF),
            ),
        ),
        FolderBackgroundPreset(
            id = "sunset",
            colors = listOf(
                Color(0xFF3D1A5C),
                Color(0xFFE76F51),
                Color(0xFFF4A261),
            ),
        ),
        FolderBackgroundPreset(
            id = "forest",
            colors = listOf(
                Color(0xFF0F2C24),
                Color(0xFF1C9961),
                Color(0xFFA8D5BA),
            ),
        ),
        FolderBackgroundPreset(
            id = "solid_base5",
            colors = listOf(Base5),
        ),
        FolderBackgroundPreset(
            id = "solid_orange",
            colors = listOf(Orange10),
        ),
        FolderBackgroundPreset(
            id = "solid_night",
            colors = listOf(Color(0xFF1A1A2E)),
        ),
    )

    fun normalizeId(id: String?): String {
        val candidate = id?.takeIf { it.isNotBlank() } ?: DEFAULT_ID
        return if (presets.any { it.id == candidate }) candidate else DEFAULT_ID
    }

    fun preset(id: String?): FolderBackgroundPreset {
        val normalized = normalizeId(id)
        return presets.first { it.id == normalized }
    }

    fun brushFor(id: String?): Brush {
        val colors = preset(id).colors
        return if (colors.size == 1) {
            SolidColor(colors.first())
        } else {
            Brush.linearGradient(colors)
        }
    }
}
