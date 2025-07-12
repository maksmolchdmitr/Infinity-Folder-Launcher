package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.VK: ImageVector
    get() {
        if (_VK != null) {
            return _VK!!
        }
        _VK = ImageVector.Builder(
            name = "VK",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(13.065f, 18.994f)
                curveTo(13.623f, 18.994f, 13.851f, 18.588f, 13.845f, 18.079f)
                curveTo(13.816f, 16.162f, 14.499f, 15.13f, 15.732f, 16.475f)
                curveTo(17.096f, 17.963f, 17.378f, 18.994f, 19.035f, 18.994f)
                horizontalLineTo(21.968f)
                curveTo(22.708f, 18.994f, 23f, 18.734f, 23f, 18.326f)
                curveTo(23f, 17.463f, 21.698f, 15.94f, 20.594f, 14.822f)
                curveTo(19.048f, 13.257f, 18.976f, 13.22f, 20.307f, 11.336f)
                curveTo(21.958f, 8.997f, 24.117f, 6f, 22.207f, 6f)
                horizontalLineTo(18.558f)
                curveTo(17.85f, 6f, 17.799f, 6.435f, 17.547f, 7.083f)
                curveTo(16.635f, 9.43f, 14.902f, 12.47f, 14.243f, 12.005f)
                curveTo(13.555f, 11.52f, 13.87f, 9.599f, 13.923f, 6.744f)
                curveTo(13.936f, 5.99f, 13.933f, 5.473f, 12.877f, 5.205f)
                curveTo(12.3f, 5.06f, 11.739f, 5f, 11.219f, 5f)
                curveTo(9.135f, 5f, 7.698f, 5.953f, 8.515f, 6.119f)
                curveTo(9.955f, 6.412f, 9.816f, 9.811f, 9.481f, 11.279f)
                curveTo(8.896f, 13.835f, 6.698f, 9.255f, 5.782f, 6.974f)
                curveTo(5.561f, 6.426f, 5.493f, 6f, 4.705f, 6f)
                horizontalLineTo(1.721f)
                curveTo(1.27f, 6f, 1f, 6.16f, 1f, 6.516f)
                curveTo(1f, 7.118f, 3.713f, 13.236f, 6.304f, 16.286f)
                curveTo(8.83f, 19.261f, 11.327f, 18.994f, 13.065f, 18.994f)
                close()
            }
        }.build()

        return _VK!!
    }

@Suppress("ObjectPropertyName")
private var _VK: ImageVector? = null
