package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Add: ImageVector
    get() {
        if (_Add != null) {
            return _Add!!
        }
        _Add = ImageVector.Builder(
            name = "Add",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(12f, 20f)
                curveTo(7.59f, 20f, 4f, 16.41f, 4f, 12f)
                curveTo(4f, 7.59f, 7.59f, 4f, 12f, 4f)
                curveTo(16.41f, 4f, 20f, 7.59f, 20f, 12f)
                curveTo(20f, 16.41f, 16.41f, 20f, 12f, 20f)
                close()
                moveTo(12f, 2f)
                curveTo(10.687f, 2f, 9.386f, 2.259f, 8.173f, 2.761f)
                curveTo(6.96f, 3.264f, 5.858f, 4f, 4.929f, 4.929f)
                curveTo(3.054f, 6.804f, 2f, 9.348f, 2f, 12f)
                curveTo(2f, 14.652f, 3.054f, 17.196f, 4.929f, 19.071f)
                curveTo(5.858f, 20f, 6.96f, 20.736f, 8.173f, 21.239f)
                curveTo(9.386f, 21.741f, 10.687f, 22f, 12f, 22f)
                curveTo(14.652f, 22f, 17.196f, 20.946f, 19.071f, 19.071f)
                curveTo(20.946f, 17.196f, 22f, 14.652f, 22f, 12f)
                curveTo(22f, 10.687f, 21.741f, 9.386f, 21.239f, 8.173f)
                curveTo(20.736f, 6.96f, 20f, 5.858f, 19.071f, 4.929f)
                curveTo(18.142f, 4f, 17.04f, 3.264f, 15.827f, 2.761f)
                curveTo(14.614f, 2.259f, 13.313f, 2f, 12f, 2f)
                close()
                moveTo(13f, 7f)
                horizontalLineTo(11f)
                verticalLineTo(11f)
                horizontalLineTo(7f)
                verticalLineTo(13f)
                horizontalLineTo(11f)
                verticalLineTo(17f)
                horizontalLineTo(13f)
                verticalLineTo(13f)
                horizontalLineTo(17f)
                verticalLineTo(11f)
                horizontalLineTo(13f)
                verticalLineTo(7f)
                close()
            }
        }.build()

        return _Add!!
    }

@Suppress("ObjectPropertyName")
private var _Add: ImageVector? = null
