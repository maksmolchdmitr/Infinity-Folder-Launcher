package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun DrawableImage(modifier: Modifier, drawable: Drawable) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            AppCompatImageView(context).apply {
                setImageDrawable(drawable)
            }
        },
        update = { imageView ->
            imageView.setImageDrawable(drawable)
        }
    )
}