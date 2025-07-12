package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Done: ImageVector
    get() {
        if (_Done != null) {
            return _Done!!
        }
        _Done = ImageVector.Builder(
            name = "Done",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(0.41f, 13.41f)
                lineTo(6f, 19f)
                lineTo(7.41f, 17.58f)
                lineTo(1.83f, 12f)
                moveTo(22.24f, 5.58f)
                lineTo(11.66f, 16.17f)
                lineTo(7.5f, 12f)
                lineTo(6.07f, 13.41f)
                lineTo(11.66f, 19f)
                lineTo(23.66f, 7f)
                moveTo(18f, 7f)
                lineTo(16.59f, 5.58f)
                lineTo(10.24f, 11.93f)
                lineTo(11.66f, 13.34f)
                lineTo(18f, 7f)
                close()
            }
        }.build()

        return _Done!!
    }

@Suppress("ObjectPropertyName")
private var _Done: ImageVector? = null
