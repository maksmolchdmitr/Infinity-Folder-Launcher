package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Yandex: ImageVector
    get() {
        if (_Yandex != null) {
            return _Yandex!!
        }
        _Yandex = ImageVector.Builder(
            name = "Yandex",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(5.544f, 2f)
                curveTo(3.581f, 2f, 2f, 3.581f, 2f, 5.544f)
                verticalLineTo(18.456f)
                curveTo(2f, 20.419f, 3.581f, 22f, 5.544f, 22f)
                horizontalLineTo(15.047f)
                lineTo(22f, 12.002f)
                lineTo(15.047f, 2f)
                horizontalLineTo(5.544f)
                verticalLineTo(2f)
                close()
                moveTo(10.866f, 5.651f)
                horizontalLineTo(13.124f)
                curveTo(13.262f, 5.651f, 13.342f, 5.702f, 13.342f, 5.821f)
                verticalLineTo(18.266f)
                curveTo(13.342f, 18.351f, 13.302f, 18.402f, 13.183f, 18.402f)
                horizontalLineTo(11.955f)
                curveTo(11.876f, 18.402f, 11.817f, 18.334f, 11.817f, 18.283f)
                verticalLineTo(13.676f)
                horizontalLineTo(10.826f)
                lineTo(8.072f, 18.283f)
                curveTo(8.033f, 18.368f, 7.954f, 18.402f, 7.835f, 18.402f)
                horizontalLineTo(6.429f)
                curveTo(6.271f, 18.402f, 6.172f, 18.283f, 6.271f, 18.13f)
                lineTo(9.301f, 13.438f)
                curveTo(7.677f, 12.825f, 6.766f, 11.584f, 6.766f, 9.901f)
                curveTo(6.766f, 7.096f, 8.647f, 5.651f, 10.866f, 5.651f)
                lineTo(10.866f, 5.651f)
                close()
                moveTo(10.806f, 6.739f)
                curveTo(9.598f, 6.739f, 8.409f, 7.606f, 8.409f, 9.731f)
                curveTo(8.409f, 11.771f, 9.677f, 12.587f, 10.984f, 12.587f)
                horizontalLineTo(11.817f)
                verticalLineTo(6.739f)
                horizontalLineTo(10.806f)
                close()
            }
        }.build()

        return _Yandex!!
    }

@Suppress("ObjectPropertyName")
private var _Yandex: ImageVector? = null
