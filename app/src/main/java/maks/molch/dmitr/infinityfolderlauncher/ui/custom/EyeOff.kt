package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.EyeOff: ImageVector
    get() {
        if (_EyeOff != null) {
            return _EyeOff!!
        }
        _EyeOff = ImageVector.Builder(
            name = "EyeOff",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(2f, 5.27f)
                lineTo(3.28f, 4f)
                lineTo(20f, 20.72f)
                lineTo(18.73f, 22f)
                lineTo(15.65f, 18.92f)
                curveTo(14.5f, 19.3f, 13.28f, 19.5f, 12f, 19.5f)
                curveTo(7f, 19.5f, 2.73f, 16.39f, 1f, 12f)
                curveTo(1.69f, 10.24f, 2.79f, 8.69f, 4.19f, 7.46f)
                lineTo(2f, 5.27f)
                close()
                moveTo(12f, 9f)
                curveTo(12.796f, 9f, 13.559f, 9.316f, 14.121f, 9.879f)
                curveTo(14.684f, 10.441f, 15f, 11.204f, 15f, 12f)
                curveTo(15f, 12.35f, 14.94f, 12.69f, 14.83f, 13f)
                lineTo(11f, 9.17f)
                curveTo(11.31f, 9.06f, 11.65f, 9f, 12f, 9f)
                close()
                moveTo(12f, 4.5f)
                curveTo(17f, 4.5f, 21.27f, 7.61f, 23f, 12f)
                curveTo(22.18f, 14.08f, 20.79f, 15.88f, 19f, 17.19f)
                lineTo(17.58f, 15.76f)
                curveTo(18.94f, 14.82f, 20.06f, 13.54f, 20.82f, 12f)
                curveTo(19.17f, 8.64f, 15.76f, 6.5f, 12f, 6.5f)
                curveTo(10.91f, 6.5f, 9.84f, 6.68f, 8.84f, 7f)
                lineTo(7.3f, 5.47f)
                curveTo(8.74f, 4.85f, 10.33f, 4.5f, 12f, 4.5f)
                close()
                moveTo(3.18f, 12f)
                curveTo(4.83f, 15.36f, 8.24f, 17.5f, 12f, 17.5f)
                curveTo(12.69f, 17.5f, 13.37f, 17.43f, 14f, 17.29f)
                lineTo(11.72f, 15f)
                curveTo(10.29f, 14.85f, 9.15f, 13.71f, 9f, 12.28f)
                lineTo(5.6f, 8.87f)
                curveTo(4.61f, 9.72f, 3.78f, 10.78f, 3.18f, 12f)
                close()
            }
        }.build()

        return _EyeOff!!
    }

@Suppress("ObjectPropertyName")
private var _EyeOff: ImageVector? = null
