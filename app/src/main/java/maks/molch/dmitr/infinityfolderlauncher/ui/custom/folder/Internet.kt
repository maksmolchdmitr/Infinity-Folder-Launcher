package maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons

val Icons.Internet: ImageVector
    get() {
        if (_Internet != null) {
            return _Internet!!
        }
        _Internet = ImageVector.Builder(
            name = "Internet",
            defaultWidth = 78.dp,
            defaultHeight = 78.dp,
            viewportWidth = 78f,
            viewportHeight = 78f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(78f)
                    verticalLineToRelative(78f)
                    horizontalLineToRelative(-78f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFF393939))) {
                    moveTo(21.667f, 0f)
                    horizontalLineTo(56.333f)
                    curveTo(68.293f, 0f, 78f, 9.707f, 78f, 21.667f)
                    verticalLineTo(56.333f)
                    curveTo(78f, 68.293f, 68.293f, 78f, 56.333f, 78f)
                    horizontalLineTo(21.667f)
                    curveTo(9.707f, 78f, 0f, 68.293f, 0f, 56.333f)
                    verticalLineTo(21.667f)
                    curveTo(0f, 9.707f, 9.707f, 0f, 21.667f, 0f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1C9961))) {
                    moveTo(53.17f, 45.5f)
                    curveTo(53.43f, 43.355f, 53.625f, 41.21f, 53.625f, 39f)
                    curveTo(53.625f, 36.79f, 53.43f, 34.645f, 53.17f, 32.5f)
                    horizontalLineTo(64.155f)
                    curveTo(64.675f, 34.58f, 65f, 36.757f, 65f, 39f)
                    curveTo(65f, 41.243f, 64.675f, 43.42f, 64.155f, 45.5f)
                    moveTo(47.417f, 63.57f)
                    curveTo(49.368f, 59.963f, 50.862f, 56.063f, 51.903f, 52f)
                    horizontalLineTo(61.49f)
                    curveTo(58.37f, 57.362f, 53.397f, 61.522f, 47.417f, 63.57f)
                    close()
                    moveTo(46.605f, 45.5f)
                    horizontalLineTo(31.395f)
                    curveTo(31.07f, 43.355f, 30.875f, 41.21f, 30.875f, 39f)
                    curveTo(30.875f, 36.79f, 31.07f, 34.612f, 31.395f, 32.5f)
                    horizontalLineTo(46.605f)
                    curveTo(46.897f, 34.612f, 47.125f, 36.79f, 47.125f, 39f)
                    curveTo(47.125f, 41.21f, 46.897f, 43.355f, 46.605f, 45.5f)
                    close()
                    moveTo(39f, 64.87f)
                    curveTo(36.303f, 60.97f, 34.125f, 56.647f, 32.792f, 52f)
                    horizontalLineTo(45.208f)
                    curveTo(43.875f, 56.647f, 41.697f, 60.97f, 39f, 64.87f)
                    close()
                    moveTo(26f, 26f)
                    horizontalLineTo(16.51f)
                    curveTo(19.597f, 20.605f, 24.603f, 16.445f, 30.55f, 14.43f)
                    curveTo(28.6f, 18.038f, 27.138f, 21.938f, 26f, 26f)
                    close()
                    moveTo(16.51f, 52f)
                    horizontalLineTo(26f)
                    curveTo(27.138f, 56.063f, 28.6f, 59.963f, 30.55f, 63.57f)
                    curveTo(24.603f, 61.522f, 19.597f, 57.362f, 16.51f, 52f)
                    close()
                    moveTo(13.845f, 45.5f)
                    curveTo(13.325f, 43.42f, 13f, 41.243f, 13f, 39f)
                    curveTo(13f, 36.757f, 13.325f, 34.58f, 13.845f, 32.5f)
                    horizontalLineTo(24.83f)
                    curveTo(24.57f, 34.645f, 24.375f, 36.79f, 24.375f, 39f)
                    curveTo(24.375f, 41.21f, 24.57f, 43.355f, 24.83f, 45.5f)
                    moveTo(39f, 13.097f)
                    curveTo(41.697f, 16.997f, 43.875f, 21.353f, 45.208f, 26f)
                    horizontalLineTo(32.792f)
                    curveTo(34.125f, 21.353f, 36.303f, 16.997f, 39f, 13.097f)
                    close()
                    moveTo(61.49f, 26f)
                    horizontalLineTo(51.903f)
                    curveTo(50.862f, 21.938f, 49.368f, 18.038f, 47.417f, 14.43f)
                    curveTo(53.397f, 16.478f, 58.37f, 20.605f, 61.49f, 26f)
                    close()
                    moveTo(39f, 6.5f)
                    curveTo(21.028f, 6.5f, 6.5f, 21.125f, 6.5f, 39f)
                    curveTo(6.5f, 47.619f, 9.924f, 55.886f, 16.019f, 61.981f)
                    curveTo(19.037f, 64.999f, 22.62f, 67.393f, 26.563f, 69.026f)
                    curveTo(30.506f, 70.659f, 34.732f, 71.5f, 39f, 71.5f)
                    curveTo(47.619f, 71.5f, 55.886f, 68.076f, 61.981f, 61.981f)
                    curveTo(68.076f, 55.886f, 71.5f, 47.619f, 71.5f, 39f)
                    curveTo(71.5f, 34.732f, 70.659f, 30.506f, 69.026f, 26.563f)
                    curveTo(67.393f, 22.62f, 64.999f, 19.037f, 61.981f, 16.019f)
                    curveTo(58.963f, 13.001f, 55.38f, 10.607f, 51.437f, 8.974f)
                    curveTo(47.494f, 7.341f, 43.268f, 6.5f, 39f, 6.5f)
                    close()
                }
            }
        }.build()

        return _Internet!!
    }

@Suppress("ObjectPropertyName")
private var _Internet: ImageVector? = null
