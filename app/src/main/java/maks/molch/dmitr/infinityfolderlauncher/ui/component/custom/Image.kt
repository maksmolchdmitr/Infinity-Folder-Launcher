package maks.molch.dmitr.infinityfolderlauncher.ui.component.custom

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

@Composable
fun Image(modifier: Modifier = Modifier, @DrawableRes resourceId: Int) {
    androidx.compose.foundation.Image(
        modifier = modifier,
        painter = painterResource(resourceId),
        contentDescription = null
    )
}

@Composable
fun Image(imageVector: ImageVector, modifier: Modifier = Modifier) {
    androidx.compose.foundation.Image(
        modifier = modifier,
        imageVector = imageVector,
        contentDescription = null
    )
}