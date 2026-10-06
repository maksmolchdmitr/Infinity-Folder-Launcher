package maks.molch.dmitr.infinityfolderlauncher.ui.component

import android.appwidget.AppWidgetHostView
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import maks.molch.dmitr.infinityfolderlauncher.LocalWidgetController
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.data.AppWidgetItem
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Cancel
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange20
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Red70
import maks.molch.dmitr.infinityfolderlauncher.widget.updateSize

@Composable
fun AppWidgetCell(
    item: AppWidgetItem,
    cellWidth: Dp,
    editModeEnabled: MutableState<Boolean>,
    selectedObjects: MutableState<Set<LauncherObject>>,
    wobble: Boolean = false,
    showDelete: Boolean = false,
    isDragging: Boolean = false,
    dragOffset: Offset = Offset.Zero,
    dragEnabled: Boolean = false,
    onDragStart: () -> Unit = {},
    onDrag: (Offset) -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDragCancel: () -> Unit = {},
    onDelete: (() -> Unit)? = null,
    onLongPress: (() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    val controller = LocalWidgetController.current
    val widgetHeight = cellWidth * item.spanRows.coerceAtLeast(1)

    val selected = editModeEnabled.value &&
        selectedObjects.value.any { it.id == item.id }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(widgetHeight)
            .zIndex(if (isDragging || selected) 1f else 0f)
            .graphicsLayer {
                if (isDragging) {
                    translationX = dragOffset.x
                    translationY = dragOffset.y
                    scaleX = 1.05f
                    scaleY = 1.05f
                    alpha = 0.92f
                    shadowElevation = 12f
                } else if (wobble) {
                    rotationZ = if (item.id.hashCode() % 2 == 0) 1.6f else -1.6f
                }
            }
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) Color(0x331C9961) else Color.Transparent)
            .then(
                if (dragEnabled) {
                    Modifier
                        .pointerInput(item.id) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = { onDragStart() },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    onDrag(dragAmount)
                                },
                                onDragEnd = onDragEnd,
                                onDragCancel = onDragCancel,
                            )
                        }
                        .clickable(onClick = onClick)
                } else if (editModeEnabled.value) {
                    Modifier.pointerInput(item.id) {
                        detectTapGestures(
                            onTap = { onClick() },
                            onLongPress = { onLongPress?.invoke() },
                        )
                    }
                } else {
                    Modifier.pointerInput(item.id) {
                        detectTapGestures(onLongPress = { onLongPress?.invoke() })
                    }
                },
            )
            .padding(top = 6.dp, end = 6.dp),
    ) {
        val info = controller?.appWidgetManager?.getAppWidgetInfo(item.appWidgetId)
        if (controller == null || info == null) {
            UnavailableWidgetPlaceholder(name = item.name)
        } else {
            AndroidView(
                factory = { ctx ->
                    controller.createView(ctx, item.appWidgetId).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                update = { view ->
                    val hostView = view as AppWidgetHostView
                    hostView.post {
                        if (hostView.width > 0 && hostView.height > 0) {
                            hostView.updateSize(hostView.width, hostView.height)
                        }
                    }
                },
            )
        }

        if ((editModeEnabled.value || showDelete) && onDelete != null) {
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
}

@Composable
private fun UnavailableWidgetPlaceholder(name: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Orange20, RoundedCornerShape(16.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.widget_unavailable, name),
            color = Base40,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        )
    }
}
