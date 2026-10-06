package maks.molch.dmitr.infinityfolderlauncher.ui.component.custom

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun DrawableImage(modifier: Modifier, drawable: Drawable) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
                setImageDrawable(drawable)
            }
        },
        update = { imageView ->
            imageView.setImageDrawable(drawable)
        },
    )
}