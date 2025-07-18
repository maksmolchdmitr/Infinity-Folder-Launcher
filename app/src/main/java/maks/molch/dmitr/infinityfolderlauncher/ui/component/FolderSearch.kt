package maks.molch.dmitr.infinityfolderlauncher.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.ClickableIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Dropdown
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.DropdownItem
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Input
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.FolderSearch
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base20

@Composable
fun FolderSearch(
    folderDao: FolderDao,
    currentFolderName: String,
    selectedObjects: MutableState<MutableSet<LauncherObject>>,
    moveObjectsEnabled: MutableState<Boolean>,
    editModeEnabled: MutableState<Boolean>,
) {
    val dropdownOn = remember { mutableStateOf(false) }
    val foldersQuery: MutableState<String> = remember { mutableStateOf("") }
    val queriedFolders: List<Folder> = folderDao.getFoldersByQuery(foldersQuery.value)
    Column(
        modifier = Modifier
            .background(shape = RoundedCornerShape(12.dp), color = Base0)
            .width(260.dp)
            .border(1.dp, color = Base20, shape = RoundedCornerShape(12.dp)),
    ) {
        Input(
            ClickableIcon(
                icon = Icons.FolderSearch,
                onClickTextConsumer = { searchText ->
                    println("Search text: $searchText")
                    foldersQuery.value = searchText
                    dropdownOn.value = true
                },
                onLongClickConsumer = { dropdownOn.value = false }
            )
        )
        if (dropdownOn.value) {
            Dropdown(
                queriedFolders.map { folder ->
                    val icon = folder.iconName?.let { Icons.folderIconByName(it) }
                        ?: R.drawable.infinity_folder_logo
                    DropdownItem(
                        icon = icon,
                        onClick = {
                            println("Click on folder '${folder.name}'")
                            onFolderClick(
                                folder.name,
                                currentFolderName,
                                folderDao,
                                selectedObjects,
                                dropdownOn,
                                moveObjectsEnabled,
                                editModeEnabled,
                            )
                        },
                        name = folder.name,
                    )
                }
            )
        }
    }
}

private fun onFolderClick(
    folderName: String,
    currentFolderName: String,
    folderDao: FolderDao,
    selectedObjects: MutableState<MutableSet<LauncherObject>>,
    dropdownOn: MutableState<Boolean>,
    moveObjectsEnabled: MutableState<Boolean>,
    editModeEnabled: MutableState<Boolean>,
) {
    folderDao.addObjectsAndSave(folderName, selectedObjects.value)
    folderDao.removeObjectsAndSave(currentFolderName, selectedObjects.value)
    dropdownOn.value = false
    moveObjectsEnabled.value = false
    editModeEnabled.value = false
}