package maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons

val Icons.Games: ImageVector
    get() {
        if (_Games != null) {
            return _Games!!
        }
        _Games = ImageVector.Builder(
            name = "Games",
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
                    moveTo(25.903f, 48.75f)
                    lineTo(16.25f, 58.5f)
                    curveTo(15.177f, 59.475f, 13.748f, 60.125f, 12.188f, 60.125f)
                    curveTo(10.679f, 60.125f, 9.232f, 59.526f, 8.166f, 58.459f)
                    curveTo(7.099f, 57.393f, 6.5f, 55.946f, 6.5f, 54.438f)
                    verticalLineTo(53.625f)
                    lineTo(9.75f, 29.64f)
                    curveTo(10.432f, 22.132f, 16.705f, 16.25f, 24.375f, 16.25f)
                    horizontalLineTo(53.625f)
                    curveTo(61.295f, 16.25f, 67.567f, 22.132f, 68.25f, 29.64f)
                    lineTo(71.5f, 53.625f)
                    verticalLineTo(54.438f)
                    curveTo(71.5f, 55.946f, 70.901f, 57.393f, 69.834f, 58.459f)
                    curveTo(68.768f, 59.526f, 67.321f, 60.125f, 65.813f, 60.125f)
                    curveTo(64.253f, 60.125f, 62.822f, 59.475f, 61.75f, 58.5f)
                    lineTo(52.097f, 48.75f)
                    horizontalLineTo(25.903f)
                    close()
                    moveTo(22.75f, 22.75f)
                    verticalLineTo(29.25f)
                    horizontalLineTo(16.25f)
                    verticalLineTo(32.5f)
                    horizontalLineTo(22.75f)
                    verticalLineTo(39f)
                    horizontalLineTo(26f)
                    verticalLineTo(32.5f)
                    horizontalLineTo(32.5f)
                    verticalLineTo(29.25f)
                    horizontalLineTo(26f)
                    verticalLineTo(22.75f)
                    horizontalLineTo(22.75f)
                    close()
                    moveTo(53.625f, 22.75f)
                    curveTo(52.979f, 22.75f, 52.359f, 23.007f, 51.901f, 23.464f)
                    curveTo(51.444f, 23.921f, 51.188f, 24.541f, 51.188f, 25.188f)
                    curveTo(51.188f, 25.834f, 51.444f, 26.454f, 51.901f, 26.911f)
                    curveTo(52.359f, 27.368f, 52.979f, 27.625f, 53.625f, 27.625f)
                    curveTo(54.271f, 27.625f, 54.891f, 27.368f, 55.349f, 26.911f)
                    curveTo(55.806f, 26.454f, 56.063f, 25.834f, 56.063f, 25.188f)
                    curveTo(56.063f, 24.541f, 55.806f, 23.921f, 55.349f, 23.464f)
                    curveTo(54.891f, 23.007f, 54.271f, 22.75f, 53.625f, 22.75f)
                    close()
                    moveTo(47.938f, 28.438f)
                    curveTo(47.291f, 28.438f, 46.671f, 28.694f, 46.214f, 29.151f)
                    curveTo(45.757f, 29.608f, 45.5f, 30.229f, 45.5f, 30.875f)
                    curveTo(45.5f, 31.521f, 45.757f, 32.141f, 46.214f, 32.599f)
                    curveTo(46.671f, 33.056f, 47.291f, 33.313f, 47.938f, 33.313f)
                    curveTo(48.584f, 33.313f, 49.204f, 33.056f, 49.661f, 32.599f)
                    curveTo(50.118f, 32.141f, 50.375f, 31.521f, 50.375f, 30.875f)
                    curveTo(50.375f, 30.229f, 50.118f, 29.608f, 49.661f, 29.151f)
                    curveTo(49.204f, 28.694f, 48.584f, 28.438f, 47.938f, 28.438f)
                    close()
                    moveTo(59.313f, 28.438f)
                    curveTo(58.666f, 28.438f, 58.046f, 28.694f, 57.589f, 29.151f)
                    curveTo(57.132f, 29.608f, 56.875f, 30.229f, 56.875f, 30.875f)
                    curveTo(56.875f, 31.521f, 57.132f, 32.141f, 57.589f, 32.599f)
                    curveTo(58.046f, 33.056f, 58.666f, 33.313f, 59.313f, 33.313f)
                    curveTo(59.959f, 33.313f, 60.579f, 33.056f, 61.036f, 32.599f)
                    curveTo(61.493f, 32.141f, 61.75f, 31.521f, 61.75f, 30.875f)
                    curveTo(61.75f, 30.229f, 61.493f, 29.608f, 61.036f, 29.151f)
                    curveTo(60.579f, 28.694f, 59.959f, 28.438f, 59.313f, 28.438f)
                    close()
                    moveTo(53.625f, 34.125f)
                    curveTo(52.979f, 34.125f, 52.359f, 34.382f, 51.901f, 34.839f)
                    curveTo(51.444f, 35.296f, 51.188f, 35.916f, 51.188f, 36.563f)
                    curveTo(51.188f, 37.209f, 51.444f, 37.829f, 51.901f, 38.286f)
                    curveTo(52.359f, 38.743f, 52.979f, 39f, 53.625f, 39f)
                    curveTo(54.271f, 39f, 54.891f, 38.743f, 55.349f, 38.286f)
                    curveTo(55.806f, 37.829f, 56.063f, 37.209f, 56.063f, 36.563f)
                    curveTo(56.063f, 35.916f, 55.806f, 35.296f, 55.349f, 34.839f)
                    curveTo(54.891f, 34.382f, 54.271f, 34.125f, 53.625f, 34.125f)
                    close()
                }
            }
        }.build()

        return _Games!!
    }

@Suppress("ObjectPropertyName")
private var _Games: ImageVector? = null
