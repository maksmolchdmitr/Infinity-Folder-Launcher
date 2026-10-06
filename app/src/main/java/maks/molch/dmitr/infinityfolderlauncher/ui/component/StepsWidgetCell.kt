package maks.molch.dmitr.infinityfolderlauncher.ui.component

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import maks.molch.dmitr.infinityfolderlauncher.InfinityFolderApp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.data.StepsWidget
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Cancel
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange20
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Red70
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StepsWidgetCell(
    item: StepsWidget,
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
    val context = LocalContext.current
    val stepsDao = (context.applicationContext as InfinityFolderApp).stepsDao
    val steps by stepsDao.stepsToday.collectAsState()
    val available by stepsDao.available.collectAsState()
    val permissionNeeded by stepsDao.permissionNeeded.collectAsState()
    val goal = item.dailyGoal.coerceAtLeast(1)
    val progress = (steps.toFloat() / goal).coerceIn(0f, 1f)
    val selected = editModeEnabled.value && selectedObjects.value.any { it.id == item.id }
    val height = cellWidth * item.spanRows.coerceAtLeast(1)

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) {
        stepsDao.start()
    }

    fun requestPermissionOrClick() {
        if (permissionNeeded && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            return
        }
        onClick()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.coerceAtLeast(88.dp))
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
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color(0x331C9961) else Orange20)
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
                        .clickable(onClick = { requestPermissionOrClick() })
                } else {
                    Modifier.pointerInput(item.id, permissionNeeded) {
                        detectTapGestures(
                            onTap = { requestPermissionOrClick() },
                            onLongPress = { onLongPress?.invoke() },
                        )
                    }
                },
            )
            .padding(top = 6.dp, end = 6.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.steps_widget_title),
                color = Base40,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
            if (permissionNeeded) {
                Text(
                    text = stringResource(R.string.steps_permission_needed),
                    color = Red70,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = stringResource(R.string.steps_permission_tap),
                    color = Base40,
                    fontSize = 12.sp,
                )
            } else {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = NumberFormat.getIntegerInstance(Locale.getDefault()).format(steps),
                        color = Base70,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(R.string.steps_unit),
                        color = Base40,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 6.dp, bottom = 6.dp),
                    )
                }
                if (!available) {
                    Text(
                        text = stringResource(R.string.steps_sensor_missing),
                        color = Red70,
                        fontSize = 12.sp,
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Green50,
                            trackColor = Base0.copy(alpha = 0.35f),
                            strokeCap = StrokeCap.Round,
                        )
                        Text(
                            text = stringResource(R.string.steps_goal, goal),
                            color = Base40,
                            fontSize = 12.sp,
                        )
                    }
                }
            }
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
