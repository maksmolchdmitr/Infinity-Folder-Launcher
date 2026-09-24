package maks.molch.dmitr.infinityfolderlauncher.ui.component

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.data.Application
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.data.WebsiteShortcut
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.DrawableImage
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.Image
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.ImageSource
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.CheckboxBlank
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.CheckboxMarked
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Search
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base10
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50

enum class SelectionStyle {
    CornerBadge,
    Checkbox,
}

@Composable
fun ObjectCell(
    context: Context,
    launcherObject: LauncherObject,
    editModeEnabled: MutableState<Boolean>,
    selectedObjects: MutableState<Set<LauncherObject>>,
    folderDao: FolderDao? = null,
    selectionStyle: SelectionStyle = SelectionStyle.CornerBadge,
    canMoveUp: Boolean = false,
    canMoveDown: Boolean = false,
    onMoveUp: (() -> Unit)? = null,
    onMoveDown: (() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    val packageManager: PackageManager = context.packageManager
    val selected = editModeEnabled.value &&
        selectedObjects.value.any { it.id == launcherObject.id }
    val state = when {
        !editModeEnabled.value -> ObjectCellState.Default
        selected -> ObjectCellState.SelectionMarked
        else -> ObjectCellState.SelectionBlank
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .clickable(onClick = onClick)
                .fillMaxWidth()
                .padding(top = 6.dp, end = 6.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(78.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier.size(70.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        when (launcherObject) {
                            is Application -> {
                                DrawableImage(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    drawable = launcherObject.getIcon(packageManager),
                                )
                            }

                            is Folder -> {
                                FolderPreviewIcon(
                                    folder = launcherObject,
                                    folderDao = folderDao,
                                    packageManager = packageManager,
                                )
                            }

                            is WebsiteShortcut -> {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Green50),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        imageVector = Icons.Search,
                                        contentDescription = null,
                                        tint = Base0,
                                        modifier = Modifier.size(32.dp),
                                    )
                                }
                            }
                        }

                        if (selected && selectionStyle == SelectionStyle.CornerBadge) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0x331C9961))
                                    .border(2.dp, Green50, RoundedCornerShape(16.dp)),
                            )
                        }
                    }

                    if (editModeEnabled.value) {
                        when (selectionStyle) {
                            SelectionStyle.CornerBadge -> {
                                SelectionBadge(
                                    marked = state == ObjectCellState.SelectionMarked,
                                    modifier = Modifier.align(Alignment.TopEnd),
                                )
                            }

                            SelectionStyle.Checkbox -> {
                                Icon(
                                    imageVector = if (selected) {
                                        Icons.CheckboxMarked
                                    } else {
                                        Icons.CheckboxBlank
                                    },
                                    contentDescription = null,
                                    tint = if (selected) Green50 else Base70,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(24.dp)
                                        .background(Base0, CircleShape),
                                )
                            }
                        }
                    }
                }

                Text(
                    modifier = Modifier
                        .height(28.dp)
                        .padding(horizontal = 2.dp),
                    text = launcherObject.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        if (
            editModeEnabled.value &&
            selected &&
            selectionStyle == SelectionStyle.CornerBadge &&
            onMoveUp != null &&
            onMoveDown != null
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ReorderChip(label = "↑", enabled = canMoveUp, onClick = onMoveUp)
                ReorderChip(label = "↓", enabled = canMoveDown, onClick = onMoveDown)
            }
        }
    }
}

@Composable
private fun SelectionBadge(marked: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(22.dp)
            .background(if (marked) Green50 else Base0, CircleShape)
            .border(
                width = 2.dp,
                color = if (marked) Green50 else Base40,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (marked) {
            Text(
                text = "✓",
                color = Base0,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 12.sp,
            )
        }
    }
}

@Composable
private fun FolderPreviewIcon(
    folder: Folder,
    folderDao: FolderDao?,
    packageManager: PackageManager,
) {
    val full = folderDao?.getById(folder.id) ?: folder
    val preview = full.launcherObjects.take(4)
    if (preview.isEmpty()) {
        val imageSource: ImageSource = ImageSource.from(
            folder.iconName?.let(Icons::folderIconByName) ?: R.drawable.infinity_folder_logo,
        )!!
        Image(modifier = Modifier.size(70.dp), imageSource = imageSource)
        return
    }

    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Base10)
            .padding(6.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            for (row in 0..1) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    for (col in 0..1) {
                        val index = row * 2 + col
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            val child = preview.getOrNull(index)
                            if (child != null) {
                                MiniChildIcon(child, packageManager)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniChildIcon(child: LauncherObject, packageManager: PackageManager) {
    when (child) {
        is Application -> DrawableImage(
            modifier = Modifier.size(24.dp),
            drawable = child.getIcon(packageManager),
        )

        is Folder -> {
            val imageSource = ImageSource.from(
                child.iconName?.let(Icons::folderIconByName) ?: R.drawable.infinity_folder_logo,
            )
            if (imageSource != null) {
                Image(modifier = Modifier.size(24.dp), imageSource = imageSource)
            }
        }

        is WebsiteShortcut -> {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Green50),
            )
        }
    }
}

@Composable
private fun ReorderChip(label: String, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(if (enabled) Green50 else Base10)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (enabled) Base0 else Base40,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

fun calcState(
    selectedObjects: MutableState<Set<LauncherObject>>,
    launcherObject: LauncherObject,
) = if (selectedObjects.value.any { it.id == launcherObject.id }) {
    ObjectCellState.SelectionMarked
} else {
    ObjectCellState.SelectionBlank
}

enum class ObjectCellState {
    Default,
    Clear,
    SelectionBlank,
    SelectionMarked,
}
