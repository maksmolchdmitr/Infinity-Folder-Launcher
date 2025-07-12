package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Widgets: ImageVector
    get() {
        if (_Widgets != null) {
            return _Widgets!!
        }
        _Widgets = ImageVector.Builder(
            name = "Widgets",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(16.7f, 4.5f)
                lineTo(19.5f, 7.3f)
                lineTo(16.7f, 10.1f)
                lineTo(13.9f, 7.3f)
                lineTo(16.7f, 4.5f)
                close()
                moveTo(9f, 5f)
                verticalLineTo(9f)
                horizontalLineTo(5f)
                verticalLineTo(5f)
                horizontalLineTo(9f)
                close()
                moveTo(19f, 15f)
                verticalLineTo(19f)
                horizontalLineTo(15f)
                verticalLineTo(15f)
                horizontalLineTo(19f)
                close()
                moveTo(16.7f, 1.7f)
                lineTo(11f, 7.3f)
                lineTo(16.7f, 13f)
                horizontalLineTo(13f)
                verticalLineTo(21f)
                horizontalLineTo(21f)
                verticalLineTo(13f)
                horizontalLineTo(16.7f)
                lineTo(22.3f, 7.3f)
                lineTo(16.7f, 1.7f)
                close()
                moveTo(11f, 3f)
                horizontalLineTo(3f)
                verticalLineTo(11f)
                horizontalLineTo(11f)
                verticalLineTo(3f)
                close()
                moveTo(9f, 15f)
                verticalLineTo(19f)
                horizontalLineTo(5f)
                verticalLineTo(15f)
                horizontalLineTo(9f)
                close()
                moveTo(11f, 13f)
                horizontalLineTo(3f)
                verticalLineTo(21f)
                horizontalLineTo(11f)
                verticalLineTo(13f)
                close()
            }
        }.build()

        return _Widgets!!
    }

@Suppress("ObjectPropertyName")
private var _Widgets: ImageVector? = null
