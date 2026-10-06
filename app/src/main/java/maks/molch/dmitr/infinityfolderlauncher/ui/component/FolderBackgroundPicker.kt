package maks.molch.dmitr.infinityfolderlauncher.ui.component

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.InfinityFolderApp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderBackgroundStore
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextBodyS
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextH4
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.FolderBackgrounds
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange20

private const val MAX_PHOTOS = 12

/**
 * @param selectedPresetId preset when no images; ignored if [selectedImageFiles] is non-empty.
 * @param selectedImageFiles app-private image file names.
 * @param selectedRotateSeconds slideshow period; null = use app default option.
 * @param useDefault selected when both preset and images are empty and [includeUseDefault].
 */
@Composable
fun FolderBackgroundPicker(
    selectedPresetId: String?,
    selectedImageFiles: List<String>,
    selectedRotateSeconds: Int?,
    onSelectPreset: (String) -> Unit,
    onSelectImages: (List<String>) -> Unit,
    onSelectRotateSeconds: (Int) -> Unit,
    onSelectUseDefault: (() -> Unit)? = null,
    includeUseDefault: Boolean = false,
    title: String = stringResource(R.string.folder_background),
) {
    val context = LocalContext.current
    val store = remember {
        (context.applicationContext as InfinityFolderApp).folderBackgroundStore
    }
    val pickImages = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(maxItems = MAX_PHOTOS),
    ) { uris ->
        if (uris.isEmpty()) return@rememberLauncherForActivityResult
        val imported = store.importFromUris(uris)
        if (imported.isEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.folder_background_import_failed),
                Toast.LENGTH_SHORT,
            ).show()
            return@rememberLauncherForActivityResult
        }
        if (imported.size < uris.size) {
            Toast.makeText(
                context,
                context.getString(R.string.folder_background_import_partial),
                Toast.LENGTH_SHORT,
            ).show()
        }
        onSelectImages(imported)
    }

    val imagesSelected = selectedImageFiles.isNotEmpty()
    val useDefaultSelected =
        includeUseDefault && selectedPresetId == null && !imagesSelected
    val previewPath = store.absolutePath(selectedImageFiles.firstOrNull())
    val rotateSeconds = selectedRotateSeconds
        ?: FolderBackgroundStore.DEFAULT_ROTATE_SECONDS

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Orange20)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TextH4(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            textAlign = TextAlign.Center,
        )
        val entries = buildList {
            if (includeUseDefault) add(PickerEntry.UseDefault)
            add(PickerEntry.Photo)
            FolderBackgrounds.presets.forEach { add(PickerEntry.Preset(it.id)) }
        }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            entries.chunked(4).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    row.forEach { entry ->
                        when (entry) {
                            PickerEntry.UseDefault -> BackgroundSwatch(
                                brush = null,
                                imagePath = null,
                                label = stringResource(R.string.folder_background_use_default),
                                badge = null,
                                selected = useDefaultSelected,
                                onClick = { onSelectUseDefault?.invoke() },
                            )

                            PickerEntry.Photo -> BackgroundSwatch(
                                brush = null,
                                imagePath = previewPath,
                                label = if (imagesSelected) {
                                    null
                                } else {
                                    stringResource(R.string.folder_background_photo)
                                },
                                badge = selectedImageFiles.size.takeIf { it > 1 }?.toString(),
                                selected = imagesSelected,
                                onClick = {
                                    pickImages.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly,
                                        ),
                                    )
                                },
                            )

                            is PickerEntry.Preset -> BackgroundSwatch(
                                brush = FolderBackgrounds.brushFor(entry.id),
                                imagePath = null,
                                label = null,
                                badge = null,
                                selected = !imagesSelected && selectedPresetId == entry.id,
                                onClick = { onSelectPreset(entry.id) },
                            )
                        }
                    }
                }
            }
        }
        if (selectedImageFiles.size >= 2) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextBodyS(
                    text = stringResource(R.string.folder_background_rotate),
                    color = Base70,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FolderBackgroundStore.ROTATE_OPTIONS_SECONDS.forEach { seconds ->
                        val selected = rotateSeconds == seconds
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (selected) Green50 else Base0)
                                .clickable { onSelectRotateSeconds(seconds) }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        ) {
                            Text(
                                text = rotateLabel(seconds),
                                color = if (selected) Base0 else Base70,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun rotateLabel(seconds: Int): String {
    return if (seconds >= 60 && seconds % 60 == 0) {
        stringResource(R.string.folder_background_rotate_minutes, seconds / 60)
    } else {
        stringResource(R.string.folder_background_rotate_seconds, seconds)
    }
}

private sealed interface PickerEntry {
    data object UseDefault : PickerEntry
    data object Photo : PickerEntry
    data class Preset(val id: String) : PickerEntry
}

@Composable
private fun BackgroundSwatch(
    brush: Brush?,
    imagePath: String?,
    label: String?,
    badge: String?,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val bitmap = remember(imagePath) {
        imagePath?.let { path ->
            runCatching {
                BitmapFactory.decodeFile(path)?.asImageBitmap()
            }.getOrNull()
        }
    }
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .then(
                when {
                    bitmap != null -> Modifier.background(Base0)
                    brush != null -> Modifier.background(brush)
                    else -> Modifier.background(Base0)
                },
            )
            .border(
                width = if (selected) 3.dp else 1.dp,
                color = if (selected) Green50 else Base40,
                shape = RoundedCornerShape(16.dp),
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
        if (label != null) {
            Text(
                text = label,
                color = Base70,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp,
                modifier = Modifier.padding(4.dp),
            )
        }
        if (badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Green50)
                    .padding(horizontal = 5.dp, vertical = 2.dp),
            ) {
                Text(
                    text = badge,
                    color = Base0,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
