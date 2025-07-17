package maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons

val Icons.Education: ImageVector
    get() {
        if (_Education != null) {
            return _Education!!
        }
        _Education = ImageVector.Builder(
            name = "Education",
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
                    moveTo(39f, 9.75f)
                    lineTo(3.25f, 29.25f)
                    lineTo(39f, 48.75f)
                    lineTo(68.25f, 32.792f)
                    verticalLineTo(55.25f)
                    horizontalLineTo(74.75f)
                    verticalLineTo(29.25f)
                    moveTo(16.25f, 42.835f)
                    verticalLineTo(55.835f)
                    lineTo(39f, 68.25f)
                    lineTo(61.75f, 55.835f)
                    verticalLineTo(42.835f)
                    lineTo(39f, 55.25f)
                    lineTo(16.25f, 42.835f)
                    close()
                }
            }
        }.build()

        return _Education!!
    }

@Suppress("ObjectPropertyName")
private var _Education: ImageVector? = null
