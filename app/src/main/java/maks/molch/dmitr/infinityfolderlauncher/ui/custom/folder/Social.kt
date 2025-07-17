package maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons

val Icons.Social: ImageVector
    get() {
        if (_Social != null) {
            return _Social!!
        }
        _Social = ImageVector.Builder(
            name = "Social",
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
                    moveTo(52f, 55.25f)
                    verticalLineTo(61.75f)
                    horizontalLineTo(6.5f)
                    verticalLineTo(55.25f)
                    curveTo(6.5f, 55.25f, 6.5f, 42.25f, 29.25f, 42.25f)
                    curveTo(52f, 42.25f, 52f, 55.25f, 52f, 55.25f)
                    close()
                    moveTo(40.625f, 24.375f)
                    curveTo(40.625f, 22.125f, 39.958f, 19.926f, 38.708f, 18.056f)
                    curveTo(37.458f, 16.185f, 35.681f, 14.727f, 33.603f, 13.866f)
                    curveTo(31.524f, 13.005f, 29.237f, 12.78f, 27.031f, 13.219f)
                    curveTo(24.824f, 13.658f, 22.798f, 14.741f, 21.207f, 16.332f)
                    curveTo(19.616f, 17.923f, 18.532f, 19.949f, 18.094f, 22.156f)
                    curveTo(17.655f, 24.363f, 17.88f, 26.65f, 18.741f, 28.728f)
                    curveTo(19.602f, 30.807f, 21.06f, 32.583f, 22.93f, 33.833f)
                    curveTo(24.801f, 35.083f, 27f, 35.75f, 29.25f, 35.75f)
                    curveTo(32.267f, 35.75f, 35.16f, 34.552f, 37.293f, 32.418f)
                    curveTo(39.427f, 30.285f, 40.625f, 27.392f, 40.625f, 24.375f)
                    close()
                    moveTo(51.805f, 42.25f)
                    curveTo(53.803f, 43.796f, 55.438f, 45.762f, 56.594f, 48.008f)
                    curveTo(57.751f, 50.254f, 58.402f, 52.726f, 58.5f, 55.25f)
                    verticalLineTo(61.75f)
                    horizontalLineTo(71.5f)
                    verticalLineTo(55.25f)
                    curveTo(71.5f, 55.25f, 71.5f, 43.453f, 51.805f, 42.25f)
                    close()
                    moveTo(48.75f, 13f)
                    curveTo(46.513f, 12.988f, 44.325f, 13.656f, 42.478f, 14.918f)
                    curveTo(44.452f, 17.676f, 45.513f, 20.983f, 45.513f, 24.375f)
                    curveTo(45.513f, 27.767f, 44.452f, 31.074f, 42.478f, 33.833f)
                    curveTo(44.325f, 35.094f, 46.513f, 35.763f, 48.75f, 35.75f)
                    curveTo(51.767f, 35.75f, 54.66f, 34.552f, 56.793f, 32.418f)
                    curveTo(58.927f, 30.285f, 60.125f, 27.392f, 60.125f, 24.375f)
                    curveTo(60.125f, 21.358f, 58.927f, 18.465f, 56.793f, 16.332f)
                    curveTo(54.66f, 14.199f, 51.767f, 13f, 48.75f, 13f)
                    close()
                }
            }
        }.build()

        return _Social!!
    }

@Suppress("ObjectPropertyName")
private var _Social: ImageVector? = null
