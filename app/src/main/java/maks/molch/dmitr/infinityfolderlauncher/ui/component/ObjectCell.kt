package maks.molch.dmitr.infinityfolderlauncher.ui.component

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.data.AppWidgetItem
import maks.molch.dmitr.infinityfolderlauncher.data.Application
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.data.StepsWidget
import maks.molch.dmitr.infinityfolderlauncher.data.WebsiteShortcut
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.DrawableImage
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.Image
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.ImageSource
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Cancel
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.CheckboxBlank
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.CheckboxMarked
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base10
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.ContrastLabel
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.LocalIconLabelStyle
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange20
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Red70

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
    isDragging: Boolean = false,
    dragOffset: Offset = Offset.Zero,
    dragEnabled: Boolean = false,
    showDelete: Boolean = false,
    wobble: Boolean = false,
    fillMaxWidth: Boolean = true,
    onDragStart: () -> Unit = {},
    onDrag: (Offset) -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDragCancel: () -> Unit = {},
    onDelete: (() -> Unit)? = null,
    onLongPress: (() -> Unit)? = null,
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

    val infinite = rememberInfiniteTransition(label = "wobble")
    val wobbleAngle by infinite.animateFloat(
        initialValue = -2.4f,
        targetValue = 2.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(140, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "wobbleAngle",
    )
    val wobbleSign = if (launcherObject.id.hashCode() % 2 == 0) 1f else -1f

    Column(
        modifier = Modifier
            .zIndex(if (isDragging) 1f else 0f)
            .graphicsLayer {
                if (isDragging) {
                    translationX = dragOffset.x
                    translationY = dragOffset.y
                    scaleX = 1.08f
                    scaleY = 1.08f
                    alpha = 0.92f
                    shadowElevation = 12f
                } else if (wobble) {
                    rotationZ = wobbleAngle * wobbleSign
                }
            }
            .then(
                if (dragEnabled) {
                    Modifier.pointerInput(launcherObject.id) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { onDragStart() },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                onDrag(dragAmount)
                            },
                            onDragEnd = onDragEnd,
                            onDragCancel = onDragCancel,
                        )
                    }.clickable(onClick = onClick)
                } else {
                    // detectTapGestures (not combinedClickable) — more reliable inside LazyGrid.
                    Modifier.pointerInput(launcherObject.id, onLongPress != null) {
                        detectTapGestures(
                            onLongPress = { onLongPress?.invoke() },
                            onTap = { onClick() },
                        )
                    }
                },
            )
            .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier.width(88.dp))
            .padding(top = 6.dp, end = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
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
                        WebsiteFaviconIcon(url = launcherObject.url, size = 70.dp, corner = 16.dp)
                    }

                    is AppWidgetItem -> {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Orange20),
                        )
                    }

                    is StepsWidget -> {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Orange20),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "👟",
                                fontSize = 28.sp,
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

            if (editModeEnabled.value || showDelete) {
                when (selectionStyle) {
                    SelectionStyle.CornerBadge -> {
                        if (editModeEnabled.value) {
                            SelectionBadge(
                                marked = state == ObjectCellState.SelectionMarked,
                                modifier = Modifier.align(Alignment.TopStart),
                            )
                        }
                        if (onDelete != null && (editModeEnabled.value || showDelete)) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Red70)
                                    .clickable { onDelete.invoke() },
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Cancel,
                                    contentDescription = null,
                                    tint = Base0,
                                    modifier = Modifier.size(14.dp),
                                )
                            }
                        }
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

        val labelStyle = LocalIconLabelStyle.current
        ContrastLabel(
            text = launcherObject.name,
            fontSizeSp = labelStyle.fontSizeSp,
            color = labelStyle.color,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
        )
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
    val iconName = full.iconName ?: folder.iconName

    if (iconName == Icons.FOLDER_ICON_APPS_PREVIEW) {
        val preview = full.launcherObjects.take(4)
        if (preview.isEmpty()) {
            FolderStaticIcon(iconName = null)
            return
        }
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(FolderIconShape)
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
        return
    }

    FolderStaticIcon(iconName = iconName)
}

@Composable
private fun FolderStaticIcon(iconName: String?) {
    val imageSource: ImageSource = when {
        iconName == null || iconName == Icons.FOLDER_ICON_DEFAULT ->
            ImageSource.from(R.drawable.infinity_folder_logo)!!
        else -> ImageSource.from(
            Icons.folderIconByName(iconName) ?: R.drawable.infinity_folder_logo,
        )!!
    }
    Image(
        modifier = Modifier.size(70.dp),
        imageSource = imageSource,
    )
}

@Composable
private fun MiniChildIcon(child: LauncherObject, packageManager: PackageManager) {
    when (child) {
        is Application -> DrawableImage(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(6.dp)),
            drawable = child.getIcon(packageManager),
        )

        is Folder -> {
            val nestedName = child.iconName?.takeIf {
                it != Icons.FOLDER_ICON_APPS_PREVIEW
            }
            val imageSource = ImageSource.from(
                nestedName?.let(Icons::folderIconByName) ?: R.drawable.infinity_folder_logo,
            )!!
            Image(
                modifier = Modifier.size(24.dp),
                imageSource = imageSource,
            )
        }

        is WebsiteShortcut -> {
            WebsiteFaviconIcon(url = child.url, size = 24.dp, corner = 6.dp)
        }

        is AppWidgetItem -> {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Orange20),
            )
        }

        is StepsWidget -> {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Orange20),
            )
        }
    }
}

private val FolderIconShape = RoundedCornerShape(19.dp)

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
