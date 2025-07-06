package maks.molch.dmitr.infinityfolderlauncher.ui.component

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val StepperItem: ImageVector
    get() {
        if (_StepperItem != null) {
            return _StepperItem!!
        }
        _StepperItem = ImageVector.Builder(
            name = "StepperItem",
            defaultWidth = 10.dp,
            defaultHeight = 10.dp,
            viewportWidth = 10f,
            viewportHeight = 10f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF868686)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(5f, 10f)
                curveTo(7.761f, 10f, 10f, 7.761f, 10f, 5f)
                curveTo(10f, 2.239f, 7.761f, 0f, 5f, 0f)
                curveTo(2.239f, 0f, 0f, 2.239f, 0f, 5f)
                curveTo(0f, 7.761f, 2.239f, 10f, 5f, 10f)
                close()
                moveTo(5f, 8f)
                curveTo(6.657f, 8f, 8f, 6.657f, 8f, 5f)
                curveTo(8f, 3.343f, 6.657f, 2f, 5f, 2f)
                curveTo(3.343f, 2f, 2f, 3.343f, 2f, 5f)
                curveTo(2f, 6.657f, 3.343f, 8f, 5f, 8f)
                close()
            }
        }.build()

        return _StepperItem!!
    }

@Suppress("ObjectPropertyName")
private var _StepperItem: ImageVector? = null



val ActiveStepperItem: ImageVector
    get() {
        if (_ActiveStepperItem != null) {
            return _ActiveStepperItem!!
        }
        _ActiveStepperItem = ImageVector.Builder(
            name = "ActiveStepperItem",
            defaultWidth = 30.dp,
            defaultHeight = 10.dp,
            viewportWidth = 30f,
            viewportHeight = 10f,
        ).apply {
            path(fill = SolidColor(Color(0xFF1C9961))) {
                moveTo(5f, 0f)
                lineTo(25f, 0f)
                arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 30f, 5f)
                lineTo(30f, 5f)
                arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 25f, 10f)
                lineTo(5f, 10f)
                arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 5f)
                lineTo(0f, 5f)
                arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 0f)
                close()
            }
        }.build()

        return _ActiveStepperItem!!
    }

@Suppress("ObjectPropertyName")
private var _ActiveStepperItem: ImageVector? = null


val ArrowIcon: ImageVector
    get() {
        if (_RightIcon != null) {
            return _RightIcon!!
        }
        _RightIcon = ImageVector.Builder(
            name = "RightIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(6f, 13f)
                verticalLineTo(11f)
                horizontalLineTo(14f)
                lineTo(10.5f, 7.5f)
                lineTo(11.92f, 6.08f)
                lineTo(17.84f, 12f)
                lineTo(11.92f, 17.92f)
                lineTo(10.5f, 16.5f)
                lineTo(14f, 13f)
                horizontalLineTo(6f)
                close()
                moveTo(22f, 12f)
                curveTo(22f, 14.652f, 20.946f, 17.196f, 19.071f, 19.071f)
                curveTo(17.196f, 20.946f, 14.652f, 22f, 12f, 22f)
                curveTo(10.687f, 22f, 9.386f, 21.741f, 8.173f, 21.239f)
                curveTo(6.96f, 20.736f, 5.858f, 20f, 4.929f, 19.071f)
                curveTo(3.054f, 17.196f, 2f, 14.652f, 2f, 12f)
                curveTo(2f, 9.348f, 3.054f, 6.804f, 4.929f, 4.929f)
                curveTo(6.804f, 3.054f, 9.348f, 2f, 12f, 2f)
                curveTo(13.313f, 2f, 14.614f, 2.259f, 15.827f, 2.761f)
                curveTo(17.04f, 3.264f, 18.142f, 4f, 19.071f, 4.929f)
                curveTo(20f, 5.858f, 20.736f, 6.96f, 21.239f, 8.173f)
                curveTo(21.741f, 9.386f, 22f, 10.687f, 22f, 12f)
                close()
                moveTo(20f, 12f)
                curveTo(20f, 9.878f, 19.157f, 7.843f, 17.657f, 6.343f)
                curveTo(16.157f, 4.843f, 14.122f, 4f, 12f, 4f)
                curveTo(9.878f, 4f, 7.843f, 4.843f, 6.343f, 6.343f)
                curveTo(4.843f, 7.843f, 4f, 9.878f, 4f, 12f)
                curveTo(4f, 14.122f, 4.843f, 16.157f, 6.343f, 17.657f)
                curveTo(7.843f, 19.157f, 9.878f, 20f, 12f, 20f)
                curveTo(14.122f, 20f, 16.157f, 19.157f, 17.657f, 17.657f)
                curveTo(19.157f, 16.157f, 20f, 14.122f, 20f, 12f)
                close()
            }
        }.build()

        return _RightIcon!!
    }

@Suppress("ObjectPropertyName")
private var _RightIcon: ImageVector? = null
