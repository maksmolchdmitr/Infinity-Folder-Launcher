package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.FolderMultiple: ImageVector
    get() {
        if (_FolderMultiple != null) {
            return _FolderMultiple!!
        }
        _FolderMultiple = ImageVector.Builder(
            name = "FolderMultiple",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(22f, 4f)
                curveTo(22.53f, 4f, 23.039f, 4.211f, 23.414f, 4.586f)
                curveTo(23.789f, 4.961f, 24f, 5.47f, 24f, 6f)
                verticalLineTo(16f)
                curveTo(24f, 16.53f, 23.789f, 17.039f, 23.414f, 17.414f)
                curveTo(23.039f, 17.789f, 22.53f, 18f, 22f, 18f)
                horizontalLineTo(6f)
                curveTo(5.47f, 18f, 4.961f, 17.789f, 4.586f, 17.414f)
                curveTo(4.211f, 17.039f, 4f, 16.53f, 4f, 16f)
                verticalLineTo(4f)
                curveTo(4f, 3.47f, 4.211f, 2.961f, 4.586f, 2.586f)
                curveTo(4.961f, 2.211f, 5.47f, 2f, 6f, 2f)
                horizontalLineTo(12f)
                lineTo(14f, 4f)
                horizontalLineTo(22f)
                close()
                moveTo(2f, 6f)
                verticalLineTo(20f)
                horizontalLineTo(20f)
                verticalLineTo(22f)
                horizontalLineTo(2f)
                curveTo(1.47f, 22f, 0.961f, 21.789f, 0.586f, 21.414f)
                curveTo(0.211f, 21.039f, 0f, 20.53f, 0f, 20f)
                verticalLineTo(11f)
                verticalLineTo(6f)
                horizontalLineTo(2f)
                close()
                moveTo(6f, 6f)
                verticalLineTo(16f)
                horizontalLineTo(22f)
                verticalLineTo(6f)
                horizontalLineTo(6f)
                close()
            }
        }.build()

        return _FolderMultiple!!
    }

@Suppress("ObjectPropertyName")
private var _FolderMultiple: ImageVector? = null
