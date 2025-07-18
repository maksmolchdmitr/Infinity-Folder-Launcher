package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Right: ImageVector
    get() {
        if (_Right != null) {
            return _Right!!
        }
        _Right = ImageVector.Builder(
            name = "Right",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(6f, 13f)
                verticalLineTo(11f)
                horizontalLineTo(14f)
                lineTo(10.5f, 7.5f)
                lineTo(11.92f, 6.08f)
                lineTo(17.84f, 12f)
                lineTo(11.92f, 17.92f)
                lineTo(10.5f, 16.5f)
                lineTo(14f, 13f)
                horizontalLineTo(6f)
                close()
                moveTo(22f, 12f)
                curveTo(22f, 14.652f, 20.946f, 17.196f, 19.071f, 19.071f)
                curveTo(17.196f, 20.946f, 14.652f, 22f, 12f, 22f)
                curveTo(10.687f, 22f, 9.386f, 21.741f, 8.173f, 21.239f)
                curveTo(6.96f, 20.736f, 5.858f, 20f, 4.929f, 19.071f)
                curveTo(3.054f, 17.196f, 2f, 14.652f, 2f, 12f)
                curveTo(2f, 9.348f, 3.054f, 6.804f, 4.929f, 4.929f)
                curveTo(6.804f, 3.054f, 9.348f, 2f, 12f, 2f)
                curveTo(13.313f, 2f, 14.614f, 2.259f, 15.827f, 2.761f)
                curveTo(17.04f, 3.264f, 18.142f, 4f, 19.071f, 4.929f)
                curveTo(20f, 5.858f, 20.736f, 6.96f, 21.239f, 8.173f)
                curveTo(21.741f, 9.386f, 22f, 10.687f, 22f, 12f)
                close()
                moveTo(20f, 12f)
                curveTo(20f, 9.878f, 19.157f, 7.843f, 17.657f, 6.343f)
                curveTo(16.157f, 4.843f, 14.122f, 4f, 12f, 4f)
                curveTo(9.878f, 4f, 7.843f, 4.843f, 6.343f, 6.343f)
                curveTo(4.843f, 7.843f, 4f, 9.878f, 4f, 12f)
                curveTo(4f, 14.122f, 4.843f, 16.157f, 6.343f, 17.657f)
                curveTo(7.843f, 19.157f, 9.878f, 20f, 12f, 20f)
                curveTo(14.122f, 20f, 16.157f, 19.157f, 17.657f, 17.657f)
                curveTo(19.157f, 16.157f, 20f, 14.122f, 20f, 12f)
                close()
            }
        }.build()

        return _Right!!
    }

@Suppress("ObjectPropertyName")
private var _Right: ImageVector? = null
