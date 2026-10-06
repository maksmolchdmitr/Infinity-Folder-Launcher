package maks.molch.dmitr.infinityfolderlauncher.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.InfinityFolderApp
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Search
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50

@Composable
fun WebsiteFaviconIcon(
    url: String,
    size: Dp = 70.dp,
    corner: Dp = 16.dp,
) {
    val context = LocalContext.current
    val cache = remember(context) {
        (context.applicationContext as InfinityFolderApp).faviconCache
    }
    val bitmap by cache.bitmapFlow(url).collectAsState()
    WebsiteFaviconIconContent(bitmap = bitmap, size = size, corner = corner)
}

@Composable
private fun WebsiteFaviconIconContent(
    bitmap: Bitmap?,
    size: Dp,
    corner: Dp,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(corner))
            .background(Green50),
        contentAlignment = Alignment.Center,
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(size),
                contentScale = ContentScale.Crop,
            )
        } else {
            Icon(
                imageVector = Icons.Search,
                contentDescription = null,
                tint = Base0,
                modifier = Modifier.size(size * 0.45f),
            )
        }
    }
}
