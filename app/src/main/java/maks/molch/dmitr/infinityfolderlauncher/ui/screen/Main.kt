package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.dao.ApplicationDao
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.ui.component.NavBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCell
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCellState
import maks.molch.dmitr.infinityfolderlauncher.ui.component.Page
import maks.molch.dmitr.infinityfolderlauncher.ui.component.TopBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.TopBarIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Cancel
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Move
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Settings
import maks.molch.dmitr.infinityfolderlauncher.utils.toastMakeTextAndShow

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
    val editModeEnabled = remember { mutableStateOf(false) }

    val launcherObjects: List<LauncherObject> =
        folderDao.getFolderByName(folderName)?.launcherObjects
            ?: run {
                val folderObjects: List<LauncherObject> = if (folderName == "MAIN_FOLDER") {
                    applicationDao.getInstalledApplications()
                } else listOf()
                folderDao.saveFolder(Folder(folderName, folderObjects))
                return@run folderObjects
            }
    Column {
        if (editModeEnabled.value) {
            TopBar(
                "Edit mode",
                leftIcon = TopBarIcon(Icons.Settings) {
                    context.toastMakeTextAndShow("Settings top bar")
                },
                firstRightIcon = TopBarIcon(Icons.Move) {
                    context.toastMakeTextAndShow("Move top bar")
                },
                secondRightIcon = TopBarIcon(
                    Icons.Cancel
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
            val selectedObjectNames: MutableState<MutableSet<String>> = remember { mutableStateOf(hashSetOf()) }
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
                    )
                }
            }
        }
        if (editModeEnabled.value) {
            NavBar(Page.Home) { page ->
                { context.toastMakeTextAndShow("${page.name} nav bar") }
            }
        }
    }
}