package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Info: ImageVector
    get() {
        if (_Info != null) {
            return _Info!!
        }
        _Info = ImageVector.Builder(
            name = "Info",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF5E5E5E))) {
                moveTo(12.3f, 7.29f)
                curveTo(12.5f, 7.11f, 12.74f, 7f, 13f, 7f)
                curveTo(13.27f, 7f, 13.5f, 7.11f, 13.71f, 7.29f)
                curveTo(13.9f, 7.5f, 14f, 7.74f, 14f, 8f)
                curveTo(14f, 8.27f, 13.9f, 8.5f, 13.71f, 8.71f)
                curveTo(13.5f, 8.9f, 13.27f, 9f, 13f, 9f)
                curveTo(12.74f, 9f, 12.5f, 8.9f, 12.3f, 8.71f)
                curveTo(12.11f, 8.5f, 12f, 8.27f, 12f, 8f)
                curveTo(12f, 7.74f, 12.11f, 7.5f, 12.3f, 7.29f)
                close()
                moveTo(9.8f, 11.97f)
                curveTo(9.8f, 11.97f, 11.97f, 10.25f, 12.76f, 10.18f)
                curveTo(13.5f, 10.12f, 13.35f, 10.97f, 13.28f, 11.41f)
                lineTo(13.27f, 11.47f)
                curveTo(13.13f, 12f, 12.96f, 12.64f, 12.79f, 13.25f)
                curveTo(12.41f, 14.64f, 12.04f, 16f, 12.13f, 16.25f)
                curveTo(12.23f, 16.59f, 12.85f, 16.16f, 13.3f, 15.86f)
                curveTo(13.36f, 15.82f, 13.41f, 15.78f, 13.46f, 15.75f)
                curveTo(13.46f, 15.75f, 13.54f, 15.67f, 13.62f, 15.78f)
                curveTo(13.64f, 15.81f, 13.66f, 15.84f, 13.68f, 15.86f)
                curveTo(13.77f, 16f, 13.82f, 16.05f, 13.7f, 16.13f)
                lineTo(13.66f, 16.15f)
                curveTo(13.44f, 16.3f, 12.5f, 16.96f, 12.12f, 17.2f)
                curveTo(11.71f, 17.47f, 10.14f, 18.37f, 10.38f, 16.62f)
                curveTo(10.59f, 15.39f, 10.87f, 14.33f, 11.09f, 13.5f)
                curveTo(11.5f, 12f, 11.68f, 11.32f, 10.76f, 11.91f)
                curveTo(10.39f, 12.13f, 10.17f, 12.27f, 10.04f, 12.36f)
                curveTo(9.93f, 12.44f, 9.92f, 12.44f, 9.85f, 12.31f)
                lineTo(9.82f, 12.25f)
                lineTo(9.77f, 12.17f)
                curveTo(9.7f, 12.07f, 9.7f, 12.06f, 9.8f, 11.97f)
                close()
                moveTo(22f, 12f)
                curveTo(22f, 17.5f, 17.5f, 22f, 12f, 22f)
                curveTo(6.5f, 22f, 2f, 17.5f, 2f, 12f)
                curveTo(2f, 6.5f, 6.5f, 2f, 12f, 2f)
                curveTo(17.5f, 2f, 22f, 6.5f, 22f, 12f)
                close()
                moveTo(20f, 12f)
                curveTo(20f, 7.58f, 16.42f, 4f, 12f, 4f)
                curveTo(7.58f, 4f, 4f, 7.58f, 4f, 12f)
                curveTo(4f, 16.42f, 7.58f, 20f, 12f, 20f)
                curveTo(16.42f, 20f, 20f, 16.42f, 20f, 12f)
                close()
            }
        }.build()

        return _Info!!
    }

@Suppress("ObjectPropertyName")
private var _Info: ImageVector? = null
