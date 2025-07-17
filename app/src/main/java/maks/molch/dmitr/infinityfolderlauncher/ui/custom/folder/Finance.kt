package maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons

val Icons.Finance: ImageVector
    get() {
        if (_Finance != null) {
            return _Finance!!
        }
        _Finance = ImageVector.Builder(
            name = "Finance",
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
                    moveTo(16.25f, 53.625f)
                    lineTo(6.5f, 63.18f)
                    verticalLineTo(35.75f)
                    horizontalLineTo(16.25f)
                    moveTo(32.5f, 47.645f)
                    lineTo(27.397f, 43.29f)
                    lineTo(22.75f, 47.58f)
                    verticalLineTo(22.75f)
                    horizontalLineTo(32.5f)
                    moveTo(48.75f, 42.25f)
                    lineTo(39f, 52f)
                    verticalLineTo(9.75f)
                    horizontalLineTo(48.75f)
                    moveTo(57.882f, 41.632f)
                    lineTo(52f, 35.75f)
                    horizontalLineTo(68.25f)
                    verticalLineTo(52f)
                    lineTo(62.432f, 46.182f)
                    lineTo(39f, 69.42f)
                    lineTo(27.722f, 59.605f)
                    lineTo(15.438f, 71.5f)
                    horizontalLineTo(6.5f)
                    lineTo(27.528f, 50.895f)
                    lineTo(39f, 60.58f)
                }
            }
        }.build()

        return _Finance!!
    }

@Suppress("ObjectPropertyName")
private var _Finance: ImageVector? = null
