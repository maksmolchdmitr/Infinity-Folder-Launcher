package maks.molch.dmitr.infinityfolderlauncher.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.getIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.ClickableIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Dropdown
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.DropdownItem
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Input
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextBodyS
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.FolderSearch
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base20
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50

@Composable
fun FolderSearch(
    folderDao: FolderDao,
    selectedFolder: MutableState<Folder?>,
) {
    val dropdownOn: MutableState<Boolean> = remember { mutableStateOf(false) }
    val foldersQuery: MutableState<String> = remember { mutableStateOf("") }
    val queriedFolders: List<Folder> = folderDao.getAllByQuery(foldersQuery.value)
    Column(
        modifier = Modifier
            .background(shape = RoundedCornerShape(12.dp), color = Base0)
            .fillMaxWidth()
            .border(1.dp, color = Base20, shape = RoundedCornerShape(12.dp)),
    ) {
        Input(
            leadingClickableIcon = selectedFolder.value?.getIcon()?.let {
                ClickableIcon(icon = it)
            },
            trailingClickableIcon = ClickableIcon(
                icon = Icons.FolderSearch,
                onClickTextConsumer = { searchText ->
                    println("Search text: $searchText")
                    foldersQuery.value = searchText
                    dropdownOn.value = true
                },
                onLongClickConsumer = { selectedFolder.value = null }
            ),
            label = {
                {
                    TextBodyS(
                        text = "Folder name",
                        color = if (selectedFolder.value == null) Base40 else Green50,
                    )
                }
            },
            inputText = selectedFolder.value?.name?.let { mutableStateOf(it) },
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (dropdownOn.value) 0.dp else 12.dp,
                bottomEnd = if (dropdownOn.value) 0.dp else 12.dp,
            ),
        )
        if (dropdownOn.value) {
            Dropdown(
                queriedFolders.map { folder ->
                    DropdownItem(
                        icon = folder.getIcon(),
                        onClick = {
                            selectedFolder.value = folder
                            dropdownOn.value = false
                        },
                        name = folder.name,
                    )
                }
            )
        }
    }
}