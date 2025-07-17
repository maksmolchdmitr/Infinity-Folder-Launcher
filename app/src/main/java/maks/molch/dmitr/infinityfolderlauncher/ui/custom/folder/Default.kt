package maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons

val Icons.Default: ImageVector
    get() {
        if (_Default != null) {
            return _Default!!
        }
        _Default = ImageVector.Builder(
            name = "Default",
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
                path(fill = SolidColor(Color.Black)) {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(78f)
                    verticalLineToRelative(78f)
                    horizontalLineToRelative(-78f)
                    close()
                }
            }
        }.build()

        return _Default!!
    }

@Suppress("ObjectPropertyName")
private var _Default: ImageVector? = null
