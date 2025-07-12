package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.CheckboxMarked: ImageVector
    get() {
        if (_CheckboxMarked != null) {
            return _CheckboxMarked!!
        }
        _CheckboxMarked = ImageVector.Builder(
            name = "CheckboxMarked",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(10f, 17f)
                lineTo(5f, 12f)
                lineTo(6.41f, 10.58f)
                lineTo(10f, 14.17f)
                lineTo(17.59f, 6.58f)
                lineTo(19f, 8f)
                moveTo(19f, 3f)
                horizontalLineTo(5f)
                curveTo(3.89f, 3f, 3f, 3.89f, 3f, 5f)
                verticalLineTo(19f)
                curveTo(3f, 19.53f, 3.211f, 20.039f, 3.586f, 20.414f)
                curveTo(3.961f, 20.789f, 4.47f, 21f, 5f, 21f)
                horizontalLineTo(19f)
                curveTo(19.53f, 21f, 20.039f, 20.789f, 20.414f, 20.414f)
                curveTo(20.789f, 20.039f, 21f, 19.53f, 21f, 19f)
                verticalLineTo(5f)
                curveTo(21f, 3.89f, 20.1f, 3f, 19f, 3f)
                close()
            }
        }.build()

        return _CheckboxMarked!!
    }

@Suppress("ObjectPropertyName")
private var _CheckboxMarked: ImageVector? = null
