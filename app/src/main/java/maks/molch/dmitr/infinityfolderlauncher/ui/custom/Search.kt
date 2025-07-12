package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Search: ImageVector
    get() {
        if (_Search != null) {
            return _Search!!
        }
        _Search = ImageVector.Builder(
            name = "Search",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(9.5f, 3f)
                curveTo(11.224f, 3f, 12.877f, 3.685f, 14.096f, 4.904f)
                curveTo(15.315f, 6.123f, 16f, 7.776f, 16f, 9.5f)
                curveTo(16f, 11.11f, 15.41f, 12.59f, 14.44f, 13.73f)
                lineTo(14.71f, 14f)
                horizontalLineTo(15.5f)
                lineTo(20.5f, 19f)
                lineTo(19f, 20.5f)
                lineTo(14f, 15.5f)
                verticalLineTo(14.71f)
                lineTo(13.73f, 14.44f)
                curveTo(12.59f, 15.41f, 11.11f, 16f, 9.5f, 16f)
                curveTo(7.776f, 16f, 6.123f, 15.315f, 4.904f, 14.096f)
                curveTo(3.685f, 12.877f, 3f, 11.224f, 3f, 9.5f)
                curveTo(3f, 7.776f, 3.685f, 6.123f, 4.904f, 4.904f)
                curveTo(6.123f, 3.685f, 7.776f, 3f, 9.5f, 3f)
                close()
                moveTo(9.5f, 5f)
                curveTo(7f, 5f, 5f, 7f, 5f, 9.5f)
                curveTo(5f, 12f, 7f, 14f, 9.5f, 14f)
                curveTo(12f, 14f, 14f, 12f, 14f, 9.5f)
                curveTo(14f, 7f, 12f, 5f, 9.5f, 5f)
                close()
            }
        }.build()

        return _Search!!
    }

@Suppress("ObjectPropertyName")
private var _Search: ImageVector? = null
