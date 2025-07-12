package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.RadioboxBlank: ImageVector
    get() {
        if (_RadioboxBlank != null) {
            return _RadioboxBlank!!
        }
        _RadioboxBlank = ImageVector.Builder(
            name = "RadioboxBlank",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(12f, 20f)
                curveTo(9.878f, 20f, 7.843f, 19.157f, 6.343f, 17.657f)
                curveTo(4.843f, 16.157f, 4f, 14.122f, 4f, 12f)
                curveTo(4f, 9.878f, 4.843f, 7.843f, 6.343f, 6.343f)
                curveTo(7.843f, 4.843f, 9.878f, 4f, 12f, 4f)
                curveTo(14.122f, 4f, 16.157f, 4.843f, 17.657f, 6.343f)
                curveTo(19.157f, 7.843f, 20f, 9.878f, 20f, 12f)
                curveTo(20f, 14.122f, 19.157f, 16.157f, 17.657f, 17.657f)
                curveTo(16.157f, 19.157f, 14.122f, 20f, 12f, 20f)
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
            }
        }.build()

        return _RadioboxBlank!!
    }

@Suppress("ObjectPropertyName")
private var _RadioboxBlank: ImageVector? = null
