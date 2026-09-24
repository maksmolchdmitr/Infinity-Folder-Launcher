package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.dao.SettingsDao
import maks.molch.dmitr.infinityfolderlauncher.data.Application
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.data.WebsiteShortcut
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCell
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCellState
import maks.molch.dmitr.infinityfolderlauncher.ui.component.SelectFolder
import maks.molch.dmitr.infinityfolderlauncher.ui.component.calcState
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.AppRemoveDialog
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.ConfirmRemove
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.NavBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Page
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBarIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Done
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Edit
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Left
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Move
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Settings
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.WallpaperGradientColors
import maks.molch.dmitr.infinityfolderlauncher.utils.MAIN_FOLDER_ID
import kotlin.math.roundToInt

@Composable
fun MainScreen(
    context: Context,
    screen: MutableState<Screen>,
    folderStack: SnapshotStateList<String>,
    currentFolderId: String,
    folderDao: FolderDao,
    settingsDao: SettingsDao,
    onEditFolder: (Folder) -> Unit,
) {
    val columns by settingsDao.mainColumns.collectAsState()
    val epoch by folderDao.epoch.collectAsState()
    val currentFolder = remember(epoch, currentFolderId) {
        folderDao.getOrCreate(currentFolderId)
    }
    val view = LocalView.current
    val gridState = rememberLazyGridState()

    val editModeEnabled = remember { mutableStateOf(false) }
    val moveObjectsEnabled = remember { mutableStateOf(false) }
    val clearObjectsEnabled = remember { mutableStateOf(false) }
    val appActionEnabled = remember { mutableStateOf(false) }
    val widgetActionEnabled = remember { mutableStateOf(false) }
    val selectedObjects: MutableState<Set<LauncherObject>> = remember { mutableStateOf(setOf()) }

    var draggingId by remember { mutableStateOf<String?>(null) }
    var dragFromIndex by remember { mutableIntStateOf(-1) }
    var dragToIndex by remember { mutableIntStateOf(-1) }
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }

    val selectedSingleFolder = selectedObjects.value.singleOrNull() as? Folder
    val selectedSingleApp = selectedObjects.value.singleOrNull() as? Application
    val selectedSingleWidget = selectedObjects.value.singleOrNull() as? WebsiteShortcut
    val launcherObjects = currentFolder.launcherObjects
    val title = if (currentFolderId == MAIN_FOLDER_ID) {
        stringResource(R.string.app_name)
    } else {
        currentFolder.name
    }
    val hasSelection = selectedObjects.value.isNotEmpty()

    fun resetDrag() {
        draggingId = null
        dragFromIndex = -1
        dragToIndex = -1
        dragOffsetX = 0f
        dragOffsetY = 0f
    }

    fun exitEditMode() {
        editModeEnabled.value = false
        selectedObjects.value = setOf()
        appActionEnabled.value = false
        widgetActionEnabled.value = false
        resetDrag()
    }

    fun finishDrag() {
        if (
            dragFromIndex >= 0 &&
            dragToIndex >= 0 &&
            dragFromIndex != dragToIndex
        ) {
            folderDao.moveObjectToIndex(currentFolderId, dragFromIndex, dragToIndex)
        }
        resetDrag()
    }

    fun targetIndexForDrag(): Int {
        if (dragFromIndex < 0 || launcherObjects.isEmpty()) return dragFromIndex
        val cell = gridState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == dragFromIndex }
            ?: gridState.layoutInfo.visibleItemsInfo.firstOrNull()
            ?: return dragFromIndex
        val cellW = cell.size.width.toFloat().coerceAtLeast(1f)
        val cellH = cell.size.height.toFloat().coerceAtLeast(1f)
        val colDelta = (dragOffsetX / cellW).roundToInt()
        val rowDelta = (dragOffsetY / cellH).roundToInt()
        return (dragFromIndex + rowDelta * columns + colDelta)
            .coerceIn(0, launcherObjects.lastIndex)
    }

    fun requestDelete(obj: LauncherObject) {
        selectedObjects.value = setOf(obj)
        when (obj) {
            is Application -> appActionEnabled.value = true
            is WebsiteShortcut -> widgetActionEnabled.value = true
            is Folder -> clearObjectsEnabled.value = true
        }
    }

    BackHandler(enabled = appActionEnabled.value || widgetActionEnabled.value) {
        appActionEnabled.value = false
        widgetActionEnabled.value = false
    }
    BackHandler(enabled = editModeEnabled.value && moveObjectsEnabled.value) {
        moveObjectsEnabled.value = false
    }
    BackHandler(
        enabled = editModeEnabled.value &&
            !moveObjectsEnabled.value &&
            !appActionEnabled.value &&
            !widgetActionEnabled.value &&
            draggingId == null,
    ) {
        exitEditMode()
    }

    val overlayOpen =
        moveObjectsEnabled.value ||
            clearObjectsEnabled.value ||
            appActionEnabled.value ||
            widgetActionEnabled.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(WallpaperGradientColors))
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (!overlayOpen) {
                when {
                    editModeEnabled.value -> {
                        TopBar(
                            label = stringResource(R.string.edit_mode),
                            leftIcon = if (!hasSelection) {
                                TopBarIcon(Icons.Settings) {
                                    screen.value = Screen.Settings
                                }
                            } else {
                                null
                            },
                            firstRightIcon = if (selectedSingleFolder != null) {
                                TopBarIcon(Icons.Edit) {
                                    onEditFolder(selectedSingleFolder)
                                    exitEditMode()
                                }
                            } else {
                                null
                            },
                            secondRightIcon = TopBarIcon(
                                icon = Icons.Move,
                                enabled = hasSelection,
                            ) { moveObjectsEnabled.value = true },
                            thirdRightIcon = TopBarIcon(
                                icon = Icons.Done,
                                color = Green50,
                            ) { exitEditMode() },
                        )
                    }

                    currentFolderId != MAIN_FOLDER_ID -> {
                        TopBar(
                            label = title,
                            leftIcon = TopBarIcon(Icons.Left) {
                                if (folderStack.size > 1) {
                                    folderStack.removeAt(folderStack.lastIndex)
                                }
                            },
                        )
                    }
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                LazyVerticalGrid(
                    state = gridState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .pointerInput(editModeEnabled.value, draggingId, overlayOpen, hasSelection) {
                            if (overlayOpen || draggingId != null) return@pointerInput
                            detectTapGestures(
                                onLongPress = {
                                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                                    if (editModeEnabled.value) {
                                        if (!hasSelection) exitEditMode()
                                    } else {
                                        editModeEnabled.value = true
                                    }
                                },
                            )
                        },
                    columns = GridCells.Fixed(columns),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = draggingId == null,
                ) {
                    itemsIndexed(launcherObjects, key = { _, item -> item.id }) { index, obj ->
                        ObjectCell(
                            context = context,
                            launcherObject = obj,
                            editModeEnabled = editModeEnabled,
                            selectedObjects = selectedObjects,
                            folderDao = folderDao,
                            isDragging = draggingId == obj.id,
                            dragOffset = if (draggingId == obj.id) {
                                Offset(dragOffsetX, dragOffsetY)
                            } else {
                                Offset.Zero
                            },
                            dragEnabled = editModeEnabled.value && !overlayOpen,
                            onDragStart = {
                                draggingId = obj.id
                                dragFromIndex = index
                                dragToIndex = index
                                dragOffsetX = 0f
                                dragOffsetY = 0f
                                selectedObjects.value = setOf(obj)
                                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            },
                            onDrag = { amount ->
                                dragOffsetX += amount.x
                                dragOffsetY += amount.y
                                dragToIndex = targetIndexForDrag()
                            },
                            onDragEnd = { finishDrag() },
                            onDragCancel = { resetDrag() },
                            onDelete = { requestDelete(obj) },
                        ) {
                            if (draggingId != null) return@ObjectCell
                            if (editModeEnabled.value) {
                                val state = calcState(selectedObjects, obj)
                                if (state == ObjectCellState.SelectionBlank) {
                                    selectedObjects.value += obj
                                } else {
                                    selectedObjects.value =
                                        selectedObjects.value.filter { it.id != obj.id }.toSet()
                                }
                                return@ObjectCell
                            }
                            when (obj) {
                                is Application -> {
                                    context.packageManager
                                        .getLaunchIntentForPackage(obj.packageName)
                                        ?.let {
                                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            context.startActivity(it)
                                        }
                                }

                                is Folder -> {
                                    if (folderStack.lastOrNull() != obj.id) {
                                        folderStack.add(obj.id)
                                    }
                                }

                                is WebsiteShortcut -> {
                                    runCatching {
                                        context.startActivity(
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(obj.url),
                                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!overlayOpen && editModeEnabled.value) {
                NavBar(Page.Home, screen)
            }
        }
    }

    if (moveObjectsEnabled.value) {
        Overlay {
            SelectFolder(
                folderDao = folderDao,
                currentFolderId = currentFolderId,
                selectedObjects = selectedObjects,
                moveObjectsEnabled = moveObjectsEnabled,
                editModeEnabled = editModeEnabled,
            )
        }
    }

    if (appActionEnabled.value && selectedSingleApp != null) {
        val app = selectedSingleApp
        Overlay(onDismiss = { appActionEnabled.value = false }) {
            AppRemoveDialog(
                appName = app.name,
                onCancel = { appActionEnabled.value = false },
                onRemoveFromFolder = {
                    folderDao.removeObjectsAndSave(currentFolderId, setOf(app))
                    selectedObjects.value = setOf()
                    appActionEnabled.value = false
                },
                onUninstall = {
                    folderDao.removeObjectsAndSave(currentFolderId, setOf(app))
                    selectedObjects.value = setOf()
                    appActionEnabled.value = false
                    runCatching {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_DELETE,
                                Uri.parse("package:${app.packageName}"),
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        )
                    }
                },
            )
        }
    }

    if (widgetActionEnabled.value && selectedSingleWidget != null) {
        val widget = selectedSingleWidget
        Overlay(onDismiss = { widgetActionEnabled.value = false }) {
            ConfirmRemove(
                mainText = stringResource(R.string.remove_widget_title, widget.name),
                descriptionText = stringResource(R.string.remove_widget_desc),
                removeText = stringResource(R.string.clear),
                onCancelClick = { widgetActionEnabled.value = false },
                onRemoveClick = {
                    folderDao.removeObjectsAndSave(currentFolderId, setOf(widget))
                    selectedObjects.value = setOf()
                    widgetActionEnabled.value = false
                },
            )
        }
    }

    if (clearObjectsEnabled.value) {
        val singleFolder = selectedObjects.value.singleOrNull() as? Folder
        Overlay(onDismiss = { clearObjectsEnabled.value = false }) {
            ConfirmRemove(
                mainText = if (singleFolder != null) {
                    stringResource(R.string.remove_folder_title, singleFolder.name)
                } else {
                    stringResource(R.string.clear_selected, selectedObjects.value.size)
                },
                descriptionText = if (singleFolder != null) {
                    stringResource(R.string.remove_folder_desc)
                } else {
                    stringResource(R.string.clear_selected_desc)
                },
                removeText = stringResource(R.string.clear),
                onCancelClick = {
                    clearObjectsEnabled.value = false
                },
                onRemoveClick = {
                    folderDao.removeObjectsAndSave(currentFolderId, selectedObjects.value)
                    selectedObjects.value = setOf()
                    clearObjectsEnabled.value = false
                },
            )
        }
    }
}

@Composable
private fun Overlay(onDismiss: (() -> Unit)? = null, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .background(Color(0x66000000))
            .clickable { onDismiss?.invoke() }
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.clickable { }) {
            content()
        }
    }
}
