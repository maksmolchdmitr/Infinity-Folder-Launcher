package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.EyeOn: ImageVector
    get() {
        if (_EyeOn != null) {
            return _EyeOn!!
        }
        _EyeOn = ImageVector.Builder(
            name = "EyeOn",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(12f, 9f)
                curveTo(12.796f, 9f, 13.559f, 9.316f, 14.121f, 9.879f)
                curveTo(14.684f, 10.441f, 15f, 11.204f, 15f, 12f)
                curveTo(15f, 12.796f, 14.684f, 13.559f, 14.121f, 14.121f)
                curveTo(13.559f, 14.684f, 12.796f, 15f, 12f, 15f)
                curveTo(11.204f, 15f, 10.441f, 14.684f, 9.879f, 14.121f)
                curveTo(9.316f, 13.559f, 9f, 12.796f, 9f, 12f)
                curveTo(9f, 11.204f, 9.316f, 10.441f, 9.879f, 9.879f)
                curveTo(10.441f, 9.316f, 11.204f, 9f, 12f, 9f)
                close()
                moveTo(12f, 4.5f)
                curveTo(17f, 4.5f, 21.27f, 7.61f, 23f, 12f)
                curveTo(21.27f, 16.39f, 17f, 19.5f, 12f, 19.5f)
                curveTo(7f, 19.5f, 2.73f, 16.39f, 1f, 12f)
                curveTo(2.73f, 7.61f, 7f, 4.5f, 12f, 4.5f)
                close()
                moveTo(3.18f, 12f)
                curveTo(4.83f, 15.36f, 8.24f, 17.5f, 12f, 17.5f)
                curveTo(15.76f, 17.5f, 19.17f, 15.36f, 20.82f, 12f)
                curveTo(19.17f, 8.64f, 15.76f, 6.5f, 12f, 6.5f)
                curveTo(8.24f, 6.5f, 4.83f, 8.64f, 3.18f, 12f)
                close()
            }
        }.build()

        return _EyeOn!!
    }

@Suppress("ObjectPropertyName")
private var _EyeOn: ImageVector? = null
