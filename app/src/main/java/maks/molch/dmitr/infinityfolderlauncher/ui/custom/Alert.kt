package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Alert: ImageVector
    get() {
        if (_Alert != null) {
            return _Alert!!
        }
        _Alert = ImageVector.Builder(
            name = "Alert",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(18.75f, 22.16f)
                lineTo(16f, 19.16f)
                lineTo(17.16f, 18f)
                lineTo(18.75f, 19.59f)
                lineTo(22.34f, 16f)
                lineTo(23.5f, 17.41f)
                lineTo(18.75f, 22.16f)
                close()
                moveTo(11f, 15f)
                horizontalLineTo(13f)
                verticalLineTo(17f)
                horizontalLineTo(11f)
                verticalLineTo(15f)
                close()
                moveTo(11f, 7f)
                horizontalLineTo(13f)
                verticalLineTo(13f)
                horizontalLineTo(11f)
                verticalLineTo(7f)
                close()
                moveTo(12f, 2f)
                curveTo(17.5f, 2f, 22f, 6.5f, 22f, 12f)
                lineTo(21.92f, 13.31f)
                curveTo(21.31f, 13.11f, 20.67f, 13f, 19.94f, 13f)
                lineTo(20f, 12f)
                curveTo(20f, 7.58f, 16.42f, 4f, 12f, 4f)
                curveTo(7.58f, 4f, 4f, 7.58f, 4f, 12f)
                curveTo(4f, 16.42f, 7.58f, 20f, 12f, 20f)
                curveTo(12.71f, 20f, 13.39f, 19.91f, 14.05f, 19.74f)
                curveTo(14.13f, 20.42f, 14.33f, 21.06f, 14.62f, 21.65f)
                curveTo(13.78f, 21.88f, 12.9f, 22f, 12f, 22f)
                curveTo(6.47f, 22f, 2f, 17.5f, 2f, 12f)
                curveTo(2f, 6.5f, 6.47f, 2f, 12f, 2f)
                close()
            }
        }.build()

        return _Alert!!
    }

@Suppress("ObjectPropertyName")
private var _Alert: ImageVector? = null
