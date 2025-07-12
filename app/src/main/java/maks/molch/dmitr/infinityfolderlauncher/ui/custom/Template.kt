package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Template: ImageVector
    get() {
        if (_Template != null) {
            return _Template!!
        }
        _Template = ImageVector.Builder(
            name = "Template",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(11.5f, 12.5f)
                moveToRelative(-7.5f, 0f)
                arcToRelative(7.5f, 7.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 15f, 0f)
                arcToRelative(7.5f, 7.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -15f, 0f)
            }
        }.build()

        return _Template!!
    }

@Suppress("ObjectPropertyName")
private var _Template: ImageVector? = null
