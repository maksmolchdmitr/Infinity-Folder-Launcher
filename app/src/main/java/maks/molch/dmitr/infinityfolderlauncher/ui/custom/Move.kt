package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Move: ImageVector
    get() {
        if (_Move != null) {
            return _Move!!
        }
        _Move = ImageVector.Builder(
            name = "Move",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(13f, 11f)
                horizontalLineTo(18f)
                lineTo(16.5f, 9.5f)
                lineTo(17.92f, 8.08f)
                lineTo(21.84f, 12f)
                lineTo(17.92f, 15.92f)
                lineTo(16.5f, 14.5f)
                lineTo(18f, 13f)
                horizontalLineTo(13f)
                verticalLineTo(18f)
                lineTo(14.5f, 16.5f)
                lineTo(15.92f, 17.92f)
                lineTo(12f, 21.84f)
                lineTo(8.08f, 17.92f)
                lineTo(9.5f, 16.5f)
                lineTo(11f, 18f)
                verticalLineTo(13f)
                horizontalLineTo(6f)
                lineTo(7.5f, 14.5f)
                lineTo(6.08f, 15.92f)
                lineTo(2.16f, 12f)
                lineTo(6.08f, 8.08f)
                lineTo(7.5f, 9.5f)
                lineTo(6f, 11f)
                horizontalLineTo(11f)
                verticalLineTo(6f)
                lineTo(9.5f, 7.5f)
                lineTo(8.08f, 6.08f)
                lineTo(12f, 2.16f)
                lineTo(15.92f, 6.08f)
                lineTo(14.5f, 7.5f)
                lineTo(13f, 6f)
                verticalLineTo(11f)
                close()
            }
        }.build()

        return _Move!!
    }

@Suppress("ObjectPropertyName")
private var _Move: ImageVector? = null
