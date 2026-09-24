package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.RenameFolderDialog
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBarIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Cancel
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Edit
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Move
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Settings
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Red70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.WallpaperColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    context: Context,
    screen: MutableState<Screen>,
    folderStack: SnapshotStateList<String>,
    currentFolderId: String,
    folderDao: FolderDao,
    settingsDao: SettingsDao,
) {
    val columns by settingsDao.mainColumns.collectAsState()
    val epoch by folderDao.epoch.collectAsState()
    val currentFolder = remember(epoch, currentFolderId) {
        folderDao.getOrCreate(currentFolderId)
    }

    val editModeEnabled = remember { mutableStateOf(false) }
    val moveObjectsEnabled = remember { mutableStateOf(false) }
    val clearObjectsEnabled = remember { mutableStateOf(false) }
    val renameFolderEnabled = remember { mutableStateOf(false) }
    val appActionEnabled = remember { mutableStateOf(false) }
    val selectedObjects: MutableState<Set<LauncherObject>> = remember { mutableStateOf(setOf()) }

    val selectedSingleFolder = selectedObjects.value.singleOrNull() as? Folder
    val selectedSingleApp = selectedObjects.value.singleOrNull() as? Application
    val launcherObjects = currentFolder.launcherObjects

    BackHandler(enabled = renameFolderEnabled.value || appActionEnabled.value) {
        renameFolderEnabled.value = false
        appActionEnabled.value = false
    }
    BackHandler(enabled = editModeEnabled.value && moveObjectsEnabled.value) {
        moveObjectsEnabled.value = false
    }
    BackHandler(
        enabled = editModeEnabled.value &&
            !moveObjectsEnabled.value &&
            !renameFolderEnabled.value &&
            !appActionEnabled.value,
    ) {
        editModeEnabled.value = false
        selectedObjects.value = setOf()
    }

    val overlayOpen =
        moveObjectsEnabled.value || renameFolderEnabled.value ||
            clearObjectsEnabled.value || appActionEnabled.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WallpaperColor)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        if (editModeEnabled.value && !overlayOpen) {
            TopBar(
                stringResource(R.string.edit_mode),
                leftIcon = TopBarIcon(Icons.Settings) {
                    screen.value = Screen.Settings
                },
                firstRightIcon = TopBarIcon(
                    icon = Icons.Edit,
                    enabled = selectedSingleFolder != null,
                ) { renameFolderEnabled.value = true },
                secondRightIcon = TopBarIcon(
                    icon = Icons.Move,
                    enabled = selectedObjects.value.isNotEmpty(),
                ) { moveObjectsEnabled.value = true },
                thirdRightIcon = TopBarIcon(
                    Icons.Cancel,
                    enabled = selectedObjects.value.isNotEmpty(),
                    color = Red70,
                ) {
                    if (selectedSingleApp != null && selectedObjects.value.size == 1) {
                        appActionEnabled.value = true
                    } else {
                        clearObjectsEnabled.value = true
                    }
                },
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .combinedClickable(
                    onLongClick = {
                        editModeEnabled.value = !editModeEnabled.value
                        if (!editModeEnabled.value) {
                            selectedObjects.value = setOf()
                            renameFolderEnabled.value = false
                            appActionEnabled.value = false
                        }
                    },
                    onClick = {},
                ),
        ) {
            LazyVerticalGrid(
                modifier = Modifier.padding(16.dp),
                columns = GridCells.Fixed(columns),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                itemsIndexed(launcherObjects, key = { _, item -> item.id }) { index, obj ->
                    ObjectCell(
                        context = context,
                        launcherObject = obj,
                        editModeEnabled = editModeEnabled,
                        selectedObjects = selectedObjects,
                        folderDao = folderDao,
                        canMoveUp = index > 0,
                        canMoveDown = index < launcherObjects.lastIndex,
                        onMoveUp = {
                            folderDao.moveObject(currentFolderId, obj.id, -1)
                        },
                        onMoveDown = {
                            folderDao.moveObject(currentFolderId, obj.id, 1)
                        },
                    ) {
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

        if (!overlayOpen) {
            NavBar(Page.Home, screen)
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

    if (renameFolderEnabled.value && selectedSingleFolder != null) {
        val folderToRename = selectedSingleFolder
        Overlay(onDismiss = { renameFolderEnabled.value = false }) {
            RenameFolderDialog(
                initialName = folderToRename.name,
                nameTaken = { candidate ->
                    currentFolder.launcherObjects.any {
                        it is Folder && it.id != folderToRename.id && it.name == candidate
                    }
                },
                onCancel = { renameFolderEnabled.value = false },
                onConfirm = { newName ->
                    if (folderDao.renameFolder(folderToRename.id, newName) ==
                        FolderDao.RenameResult.Ok
                    ) {
                        selectedObjects.value = setOf()
                        renameFolderEnabled.value = false
                    }
                },
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
                    editModeEnabled.value = false
                },
                onUninstall = {
                    folderDao.removeObjectsAndSave(currentFolderId, setOf(app))
                    selectedObjects.value = setOf()
                    appActionEnabled.value = false
                    editModeEnabled.value = false
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

    if (clearObjectsEnabled.value) {
        Overlay(onDismiss = { clearObjectsEnabled.value = false }) {
            ConfirmRemove(
                mainText = stringResource(
                    R.string.clear_selected,
                    selectedObjects.value.size,
                ),
                descriptionText = stringResource(R.string.clear_selected_desc),
                removeText = stringResource(R.string.clear),
                onCancelClick = {
                    selectedObjects.value = setOf()
                    clearObjectsEnabled.value = false
                    editModeEnabled.value = false
                },
                onRemoveClick = {
                    folderDao.removeObjectsAndSave(currentFolderId, selectedObjects.value)
                    selectedObjects.value = setOf()
                    clearObjectsEnabled.value = false
                    editModeEnabled.value = false
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
