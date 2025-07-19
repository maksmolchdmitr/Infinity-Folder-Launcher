package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.MainActivity
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.dao.putFolderName
import maks.molch.dmitr.infinityfolderlauncher.data.Application
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCell
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCellState
import maks.molch.dmitr.infinityfolderlauncher.ui.component.SelectFolder
import maks.molch.dmitr.infinityfolderlauncher.ui.component.calcState
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.ConfirmRemove
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.NavBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Page
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBarIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Cancel
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Move
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Settings
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Red70
import maks.molch.dmitr.infinityfolderlauncher.utils.toastMakeTextAndShow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    context: Context,
    screen: MutableState<Screen>,
    currentFolderName: String,
    folderDao: FolderDao
) {
    val objectNumberOnTheRow = 4

    val editModeEnabled = remember {
        mutableStateOf(false)
    }
    val moveObjectsEnabled = remember {
        mutableStateOf(false)
    }
    val clearObjectsEnabled = remember {
        mutableStateOf(false)
    }
    val selectedObjects: MutableState<Set<LauncherObject>> = remember {
        mutableStateOf(
            setOf()
        )
    }

    val launcherObjects: List<LauncherObject> =
        folderDao.getFolderOrSaveByName(currentFolderName).launcherObjects

    BackHandler(enabled = editModeEnabled.value && moveObjectsEnabled.value) {
        moveObjectsEnabled.value = false
    }
    BackHandler(enabled = editModeEnabled.value && !moveObjectsEnabled.value) {
        editModeEnabled.value = false
    }

    Column(
        modifier = Modifier
            .blur(if (moveObjectsEnabled.value) 4.dp else 0.dp)
            .clickable(enabled = !moveObjectsEnabled.value) {}
    ) {
        if (editModeEnabled.value && !moveObjectsEnabled.value) {
            TopBar(
                "Edit mode",
                leftIcon = TopBarIcon(Icons.Settings) {
                    context.toastMakeTextAndShow("Settings top bar")
                },
                firstRightIcon = TopBarIcon(
                    icon = Icons.Move,
                    enabled = selectedObjects.value.isNotEmpty(),
                ) {
                    moveObjectsEnabled.value = true
                },
                secondRightIcon = TopBarIcon(
                    Icons.Cancel,
                    enabled = selectedObjects.value.isNotEmpty(),
                    color = Red70,
                ) {
                    clearObjectsEnabled.value = true
                },
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .combinedClickable(
                    onLongClick = {
                        editModeEnabled.value = !editModeEnabled.value
                    },
                    onClick = {}
                )
                .paint(
                    painter = painterResource(R.drawable.infinity_folder_logo),
                    contentScale = ContentScale.Crop,
                ),
            contentAlignment = Alignment.Center,
        ) {
            LazyVerticalGrid(
                modifier = Modifier.padding(16.dp),
                columns = GridCells.Fixed(objectNumberOnTheRow),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                items(launcherObjects) { launcherObject ->
                    ObjectCell(
                        context,
                        launcherObject,
                        editModeEnabled,
                        selectedObjects,
                    ) {
                        if (editModeEnabled.value) {
                            println("Selected objects: $selectedObjects")
                            val state: ObjectCellState = calcState(selectedObjects, launcherObject)
                            if (state == ObjectCellState.SelectionBlank) {
                                selectedObjects.value += launcherObject
                            } else {
                                selectedObjects.value = selectedObjects.value
                                    .filter { it.name != launcherObject.name }
                                    .toSet()
                            }

                            return@ObjectCell
                        }

                        when (launcherObject) {
                            is Application -> {
                                val packageManager: PackageManager = context.packageManager
                                packageManager.getLaunchIntentForPackage(launcherObject.packageName)
                                    ?.let { intent ->
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        context.startActivity(intent)
                                    }
                            }

                            is Folder -> {
                                val intent = Intent(context, MainActivity::class.java)
                                intent.putFolderName(launcherObject.name)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
        if (editModeEnabled.value && !moveObjectsEnabled.value) {
            NavBar(Page.Home, context, screen)
        }
    }
    if (moveObjectsEnabled.value) {
        Box(
            modifier = Modifier
                .background(Color.Unspecified)
                .clickable {
                    moveObjectsEnabled.value = false
                }
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            SelectFolder(
                folderDao,
                currentFolderName,
                selectedObjects,
                moveObjectsEnabled,
                editModeEnabled,
            )
        }
    }
    if (clearObjectsEnabled.value) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Unspecified)
                .clickable {
                    moveObjectsEnabled.value = false
                }
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            ConfirmRemove(
                mainText = "Clear ${selectedObjects.value.size} selected object${
                    if (selectedObjects.value.size == 1) "" else "s"
                }",
                descriptionText = "Do you really want to clear all these objects?",
                removeText = "Clear",
                onCancelClick = {
                    selectedObjects.value = setOf()
                    clearObjectsEnabled.value = false
                    editModeEnabled.value = false
                },
                onRemoveClick = {
                    folderDao.removeObjectsAndSave(currentFolderName, selectedObjects.value)
                    selectedObjects.value = setOf()
                    clearObjectsEnabled.value = false
                    editModeEnabled.value = false
                },
            )
        }
    }
}