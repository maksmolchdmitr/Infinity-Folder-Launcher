package maks.molch.dmitr.infinityfolderlauncher.ui.component.custom

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource


@Composable
fun Image(modifier: Modifier, @DrawableRes resourceId: Int) {
    androidx.compose.foundation.Image(
        modifier = modifier,
        painter = painterResource(resourceId),
        contentDescription = null
    )
}