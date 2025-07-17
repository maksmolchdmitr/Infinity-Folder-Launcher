package maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons

val Icons.Work: ImageVector
    get() {
        if (_Work != null) {
            return _Work!!
        }
        _Work = ImageVector.Builder(
            name = "Work",
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
                    moveTo(36.292f, 29.792f)
                    curveTo(36.292f, 35.777f, 31.444f, 40.625f, 25.458f, 40.625f)
                    curveTo(19.473f, 40.625f, 14.625f, 35.777f, 14.625f, 29.792f)
                    curveTo(14.625f, 23.806f, 19.473f, 18.958f, 25.458f, 18.958f)
                    curveTo(31.444f, 18.958f, 36.292f, 23.806f, 36.292f, 29.792f)
                    close()
                    moveTo(36.292f, 47.992f)
                    verticalLineTo(62.292f)
                    horizontalLineTo(6.5f)
                    verticalLineTo(56.875f)
                    curveTo(6.5f, 50.89f, 14.977f, 46.042f, 25.458f, 46.042f)
                    curveTo(29.521f, 46.042f, 33.231f, 46.773f, 36.292f, 47.992f)
                    close()
                    moveTo(71.5f, 62.292f)
                    horizontalLineTo(41.708f)
                    verticalLineTo(16.25f)
                    horizontalLineTo(71.5f)
                    verticalLineTo(62.292f)
                    close()
                    moveTo(49.833f, 39.271f)
                    curveTo(49.833f, 35.533f, 52.867f, 32.5f, 56.604f, 32.5f)
                    curveTo(60.342f, 32.5f, 63.375f, 35.533f, 63.375f, 39.271f)
                    curveTo(63.375f, 43.008f, 60.342f, 46.042f, 56.604f, 46.042f)
                    curveTo(52.867f, 46.042f, 49.833f, 43.008f, 49.833f, 39.271f)
                    close()
                    moveTo(66.083f, 27.083f)
                    curveTo(63.104f, 27.083f, 60.667f, 24.673f, 60.667f, 21.667f)
                    horizontalLineTo(52.542f)
                    curveTo(52.542f, 24.673f, 50.131f, 27.083f, 47.125f, 27.083f)
                    verticalLineTo(51.458f)
                    curveTo(50.131f, 51.458f, 52.542f, 53.896f, 52.542f, 56.875f)
                    horizontalLineTo(60.667f)
                    curveTo(60.667f, 53.896f, 63.104f, 51.458f, 66.083f, 51.458f)
                    verticalLineTo(27.083f)
                    close()
                }
            }
        }.build()

        return _Work!!
    }

@Suppress("ObjectPropertyName")
private var _Work: ImageVector? = null
