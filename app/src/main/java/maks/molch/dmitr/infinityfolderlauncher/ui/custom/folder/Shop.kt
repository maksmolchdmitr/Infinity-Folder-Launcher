package maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons

val Icons.Shop: ImageVector
    get() {
        if (_Shop != null) {
            return _Shop!!
        }
        _Shop = ImageVector.Builder(
            name = "Shop",
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
                    moveTo(56.55f, 56.55f)
                    curveTo(53.303f, 56.55f, 50.7f, 59.153f, 50.7f, 62.4f)
                    curveTo(50.7f, 63.951f, 51.316f, 65.439f, 52.413f, 66.537f)
                    curveTo(53.511f, 67.634f, 54.999f, 68.25f, 56.55f, 68.25f)
                    curveTo(58.102f, 68.25f, 59.59f, 67.634f, 60.687f, 66.537f)
                    curveTo(61.784f, 65.439f, 62.4f, 63.951f, 62.4f, 62.4f)
                    curveTo(62.4f, 59.153f, 59.768f, 56.55f, 56.55f, 56.55f)
                    close()
                    moveTo(9.75f, 9.75f)
                    verticalLineTo(15.6f)
                    horizontalLineTo(15.6f)
                    lineTo(26.13f, 37.801f)
                    lineTo(22.152f, 44.967f)
                    curveTo(21.713f, 45.786f, 21.45f, 46.751f, 21.45f, 47.775f)
                    curveTo(21.45f, 49.326f, 22.066f, 50.814f, 23.163f, 51.912f)
                    curveTo(24.26f, 53.009f, 25.749f, 53.625f, 27.3f, 53.625f)
                    horizontalLineTo(62.4f)
                    verticalLineTo(47.775f)
                    horizontalLineTo(28.528f)
                    curveTo(28.335f, 47.775f, 28.149f, 47.698f, 28.011f, 47.561f)
                    curveTo(27.874f, 47.424f, 27.797f, 47.238f, 27.797f, 47.044f)
                    curveTo(27.797f, 46.897f, 27.826f, 46.78f, 27.885f, 46.693f)
                    lineTo(30.517f, 41.925f)
                    horizontalLineTo(52.309f)
                    curveTo(54.502f, 41.925f, 56.433f, 40.696f, 57.428f, 38.912f)
                    lineTo(67.899f, 19.987f)
                    curveTo(68.104f, 19.52f, 68.25f, 19.022f, 68.25f, 18.525f)
                    curveTo(68.25f, 17.749f, 67.942f, 17.005f, 67.393f, 16.457f)
                    curveTo(66.845f, 15.908f, 66.101f, 15.6f, 65.325f, 15.6f)
                    horizontalLineTo(22.064f)
                    lineTo(19.315f, 9.75f)
                    moveTo(27.3f, 56.55f)
                    curveTo(24.053f, 56.55f, 21.45f, 59.153f, 21.45f, 62.4f)
                    curveTo(21.45f, 63.951f, 22.066f, 65.439f, 23.163f, 66.537f)
                    curveTo(24.26f, 67.634f, 25.749f, 68.25f, 27.3f, 68.25f)
                    curveTo(28.851f, 68.25f, 30.34f, 67.634f, 31.437f, 66.537f)
                    curveTo(32.534f, 65.439f, 33.15f, 63.951f, 33.15f, 62.4f)
                    curveTo(33.15f, 59.153f, 30.517f, 56.55f, 27.3f, 56.55f)
                    close()
                }
            }
        }.build()

        return _Shop!!
    }

@Suppress("ObjectPropertyName")
private var _Shop: ImageVector? = null
