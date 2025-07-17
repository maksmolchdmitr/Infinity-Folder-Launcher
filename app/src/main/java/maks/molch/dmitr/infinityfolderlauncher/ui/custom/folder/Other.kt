package maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons

val Icons.Other: ImageVector
    get() {
        if (_Other != null) {
            return _Other!!
        }
        _Other = ImageVector.Builder(
            name = "Other",
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
                    moveTo(52f, 39f)
                    curveTo(52f, 37.276f, 52.685f, 35.623f, 53.904f, 34.404f)
                    curveTo(55.123f, 33.185f, 56.776f, 32.5f, 58.5f, 32.5f)
                    curveTo(60.224f, 32.5f, 61.877f, 33.185f, 63.096f, 34.404f)
                    curveTo(64.315f, 35.623f, 65f, 37.276f, 65f, 39f)
                    curveTo(65f, 40.724f, 64.315f, 42.377f, 63.096f, 43.596f)
                    curveTo(61.877f, 44.815f, 60.224f, 45.5f, 58.5f, 45.5f)
                    curveTo(56.776f, 45.5f, 55.123f, 44.815f, 53.904f, 43.596f)
                    curveTo(52.685f, 42.377f, 52f, 40.724f, 52f, 39f)
                    close()
                    moveTo(32.5f, 39f)
                    curveTo(32.5f, 37.276f, 33.185f, 35.623f, 34.404f, 34.404f)
                    curveTo(35.623f, 33.185f, 37.276f, 32.5f, 39f, 32.5f)
                    curveTo(40.724f, 32.5f, 42.377f, 33.185f, 43.596f, 34.404f)
                    curveTo(44.815f, 35.623f, 45.5f, 37.276f, 45.5f, 39f)
                    curveTo(45.5f, 40.724f, 44.815f, 42.377f, 43.596f, 43.596f)
                    curveTo(42.377f, 44.815f, 40.724f, 45.5f, 39f, 45.5f)
                    curveTo(37.276f, 45.5f, 35.623f, 44.815f, 34.404f, 43.596f)
                    curveTo(33.185f, 42.377f, 32.5f, 40.724f, 32.5f, 39f)
                    close()
                    moveTo(13f, 39f)
                    curveTo(13f, 37.276f, 13.685f, 35.623f, 14.904f, 34.404f)
                    curveTo(16.123f, 33.185f, 17.776f, 32.5f, 19.5f, 32.5f)
                    curveTo(21.224f, 32.5f, 22.877f, 33.185f, 24.096f, 34.404f)
                    curveTo(25.315f, 35.623f, 26f, 37.276f, 26f, 39f)
                    curveTo(26f, 40.724f, 25.315f, 42.377f, 24.096f, 43.596f)
                    curveTo(22.877f, 44.815f, 21.224f, 45.5f, 19.5f, 45.5f)
                    curveTo(17.776f, 45.5f, 16.123f, 44.815f, 14.904f, 43.596f)
                    curveTo(13.685f, 42.377f, 13f, 40.724f, 13f, 39f)
                    close()
                }
            }
        }.build()

        return _Other!!
    }

@Suppress("ObjectPropertyName")
private var _Other: ImageVector? = null
