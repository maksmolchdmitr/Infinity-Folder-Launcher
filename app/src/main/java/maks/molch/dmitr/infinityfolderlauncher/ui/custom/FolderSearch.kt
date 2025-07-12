package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.FolderSearch: ImageVector
    get() {
        if (_FolderSearch != null) {
            return _FolderSearch!!
        }
        _FolderSearch = ImageVector.Builder(
            name = "FolderSearch",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(16.5f, 12f)
                curveTo(19f, 12f, 21f, 14f, 21f, 16.5f)
                curveTo(21f, 17.38f, 20.75f, 18.21f, 20.31f, 18.9f)
                lineTo(23.39f, 22f)
                lineTo(22f, 23.39f)
                lineTo(18.88f, 20.32f)
                curveTo(18.19f, 20.75f, 17.37f, 21f, 16.5f, 21f)
                curveTo(14f, 21f, 12f, 19f, 12f, 16.5f)
                curveTo(12f, 14f, 14f, 12f, 16.5f, 12f)
                close()
                moveTo(16.5f, 14f)
                curveTo(15.837f, 14f, 15.201f, 14.263f, 14.732f, 14.732f)
                curveTo(14.263f, 15.201f, 14f, 15.837f, 14f, 16.5f)
                curveTo(14f, 17.163f, 14.263f, 17.799f, 14.732f, 18.268f)
                curveTo(15.201f, 18.737f, 15.837f, 19f, 16.5f, 19f)
                curveTo(17.163f, 19f, 17.799f, 18.737f, 18.268f, 18.268f)
                curveTo(18.737f, 17.799f, 19f, 17.163f, 19f, 16.5f)
                curveTo(19f, 15.837f, 18.737f, 15.201f, 18.268f, 14.732f)
                curveTo(17.799f, 14.263f, 17.163f, 14f, 16.5f, 14f)
                close()
                moveTo(9f, 4f)
                lineTo(11f, 6f)
                horizontalLineTo(19f)
                curveTo(19.53f, 6f, 20.039f, 6.211f, 20.414f, 6.586f)
                curveTo(20.789f, 6.961f, 21f, 7.47f, 21f, 8f)
                verticalLineTo(11.81f)
                curveTo(19.83f, 10.69f, 18.25f, 10f, 16.5f, 10f)
                curveTo(14.776f, 10f, 13.123f, 10.685f, 11.904f, 11.904f)
                curveTo(10.685f, 13.123f, 10f, 14.776f, 10f, 16.5f)
                curveTo(10f, 17.79f, 10.37f, 19f, 11f, 20f)
                horizontalLineTo(3f)
                curveTo(1.89f, 20f, 1f, 19.1f, 1f, 18f)
                verticalLineTo(6f)
                curveTo(1f, 4.89f, 1.89f, 4f, 3f, 4f)
                horizontalLineTo(9f)
                close()
            }
        }.build()

        return _FolderSearch!!
    }

@Suppress("ObjectPropertyName")
private var _FolderSearch: ImageVector? = null
