package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import android.annotation.SuppressLint
import android.content.Context
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
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.dao.ApplicationDao
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.ui.component.FolderSearch
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCell
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCellState
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

const val DEBUG_ENABLED = true

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    context: Context,
    folderName: String,
    folderDao: FolderDao,
    applicationDao: ApplicationDao
) {
    val objectNumberOnTheRow = 4
    val editModeEnabled = remember {
        mutableStateOf(false || DEBUG_ENABLED)
    }
    val objectsWasSelected: MutableState<Boolean> = remember {
        mutableStateOf(false || DEBUG_ENABLED)
    }
    val moveObjectsEnabled = remember {
        mutableStateOf(false || DEBUG_ENABLED)
    }
    val selectedObjectNames: MutableState<MutableSet<String>> = remember {
        mutableStateOf(hashSetOf())
    }

    BackHandler(enabled = editModeEnabled.value && moveObjectsEnabled.value) {
        moveObjectsEnabled.value = false
    }
    BackHandler(enabled = editModeEnabled.value && !moveObjectsEnabled.value) {
        editModeEnabled.value = false
    }

    val launcherObjects: List<LauncherObject> =
        folderDao.getFolderByName(folderName)?.launcherObjects
            ?: run {
                val folderObjects: List<LauncherObject> = if (folderName == "MAIN_FOLDER") {
                    applicationDao.getInstalledApplications()
                } else listOf()
                folderDao.saveFolder(Folder(folderName, folderObjects))
                return@run folderObjects
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
                    enabled = objectsWasSelected,
                ) {
                    context.toastMakeTextAndShow("Move top bar")
                    moveObjectsEnabled.value = true
                },
                secondRightIcon = TopBarIcon(
                    Icons.Cancel,
                    enabled = objectsWasSelected,
                    color = Red70,
                ) {
                    context.toastMakeTextAndShow("Cancel top bar")
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
                items(launcherObjects) {
                    ObjectCell(
                        context,
                        it,
                        editModeEnabled,
                        remember { mutableStateOf(ObjectCellState.SelectionBlank) },
                        selectedObjectNames,
                        objectsWasSelected,
                    )
                }
            }
        }
        if (editModeEnabled.value && !moveObjectsEnabled.value) {
            NavBar(Page.Home) { page ->
                { context.toastMakeTextAndShow("${page.name} nav bar") }
            }
        }
    }
    if (moveObjectsEnabled.value) {
        Box(
            modifier = Modifier
                .background(Color.Unspecified)
                .clickable { }
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            FolderSearch()
        }
    }
}