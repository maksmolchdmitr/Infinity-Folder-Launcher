package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Edit: ImageVector
    get() {
        if (_Edit != null) {
            return _Edit!!
        }
        _Edit = ImageVector.Builder(
            name = "Edit",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(15.84f, 10.2f)
                lineTo(14.83f, 11.21f)
                lineTo(12.76f, 9.18f)
                lineTo(13.77f, 8.16f)
                curveTo(13.97f, 7.95f, 14.31f, 7.94f, 14.55f, 8.16f)
                lineTo(15.84f, 9.41f)
                curveTo(16.05f, 9.62f, 16.06f, 9.96f, 15.84f, 10.2f)
                close()
                moveTo(8f, 13.91f)
                lineTo(12.17f, 9.72f)
                lineTo(14.24f, 11.8f)
                lineTo(10.08f, 16f)
                horizontalLineTo(8f)
                verticalLineTo(13.91f)
                close()
                moveTo(13f, 2f)
                verticalLineTo(4f)
                curveTo(17.39f, 4.54f, 20.5f, 8.53f, 19.96f, 12.92f)
                curveTo(19.5f, 16.56f, 16.64f, 19.43f, 13f, 19.88f)
                verticalLineTo(21.88f)
                curveTo(18.5f, 21.28f, 22.45f, 16.34f, 21.85f, 10.85f)
                curveTo(21.33f, 6.19f, 17.66f, 2.5f, 13f, 2f)
                close()
                moveTo(11f, 2f)
                curveTo(9.04f, 2.18f, 7.19f, 2.95f, 5.67f, 4.2f)
                lineTo(7.1f, 5.74f)
                curveTo(8.22f, 4.84f, 9.57f, 4.26f, 11f, 4.06f)
                verticalLineTo(2.06f)
                moveTo(4.26f, 5.67f)
                curveTo(3f, 7.19f, 2.24f, 9.04f, 2.05f, 11f)
                horizontalLineTo(4.05f)
                curveTo(4.24f, 9.58f, 4.8f, 8.23f, 5.69f, 7.1f)
                lineTo(4.26f, 5.67f)
                close()
                moveTo(2.06f, 13f)
                curveTo(2.26f, 14.96f, 3.03f, 16.81f, 4.27f, 18.33f)
                lineTo(5.69f, 16.9f)
                curveTo(4.81f, 15.77f, 4.24f, 14.42f, 4.06f, 13f)
                horizontalLineTo(2.06f)
                close()
                moveTo(7.06f, 18.37f)
                lineTo(5.67f, 19.74f)
                curveTo(7.18f, 21f, 9.04f, 21.79f, 11f, 22f)
                verticalLineTo(20f)
                curveTo(9.58f, 19.82f, 8.23f, 19.25f, 7.1f, 18.37f)
                horizontalLineTo(7.06f)
                close()
            }
        }.build()

        return _Edit!!
    }

@Suppress("ObjectPropertyName")
private var _Edit: ImageVector? = null
