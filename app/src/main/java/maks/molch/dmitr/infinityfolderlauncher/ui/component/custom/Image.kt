package maks.molch.dmitr.infinityfolderlauncher.ui.component.custom

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

@Composable
fun Image(@DrawableRes resourceId: Int, modifier: Modifier = Modifier) {
    Image(ImageSource.Resource(resourceId), modifier)
}

@Composable
fun Image(imageVector: ImageVector, modifier: Modifier = Modifier) {
    Image(ImageSource.Vector(imageVector), modifier)
}

@Composable
fun Image(imageSource: ImageSource, modifier: Modifier = Modifier) {
    when (imageSource) {
        is ImageSource.Vector -> {
            androidx.compose.foundation.Image(
                modifier = modifier,
                imageVector = imageSource.imageVector,
                contentDescription = null
            )
        }

        is ImageSource.Resource -> {
            androidx.compose.foundation.Image(
                modifier = modifier,
                painter = painterResource(imageSource.resourceId),
                contentDescription = null
            )
        }
    }
}

sealed class ImageSource {
    data class Resource(@DrawableRes val resourceId: Int) : ImageSource()
    data class Vector(val imageVector: ImageVector) : ImageSource()

    companion object {
        fun from(icon: Any): ImageSource? {
            return when (icon) {
                is Int -> Resource(icon)
                is ImageVector -> Vector(icon)
                else -> null
            }
        }
    }
}