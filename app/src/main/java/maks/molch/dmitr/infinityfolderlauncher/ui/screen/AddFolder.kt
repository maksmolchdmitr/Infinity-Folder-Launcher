package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.ClickableIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Input
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextBodyS
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextH4
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBarIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.Image
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.ImageSource
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.toImageSource
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Add
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Cancel
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base90
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange20
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Red50

@Composable
fun AddFolder(
    screen: MutableState<Screen>,
    currentFolderName: String,
    folderDao: FolderDao
) {
    val inputText: MutableState<String> = remember { mutableStateOf("") }
    val selectedNamedIcon: MutableState<Pair<String, ImageSource>?> = remember {
        mutableStateOf(
            "Default" to R.drawable.infinity_folder_logo.toImageSource()
        )
    }
    val folderNames: List<String> =
        folderDao.getOrSaveByName(currentFolderName).launcherObjects
            .mapNotNull { it as? Folder }
            .map { it.name }
    val folderAlreadyExist = inputText.value in folderNames

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopBar(
            label = "Add Folder",
            secondRightIcon = TopBarIcon(
                icon = Icons.Add,
                color = Base70,
                enabled = inputText.value.isNotBlank() && !folderAlreadyExist,
            ) {
                val currentFolder: Folder = folderDao.getOrSaveByName(currentFolderName)
                val newFolder: Folder = folderDao.put(
                    Folder(
                        name = inputText.value,
                        iconName = selectedNamedIcon.value?.first,
                    )
                )
                folderDao.save(
                    currentFolder.copy(
                        launcherObjects = currentFolder.launcherObjects + newFolder
                    )
                )
                screen.value = Screen.Main
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            FolderInput(inputText, folderAlreadyExist)
            SelectFolderIcon(selectedNamedIcon, folderAlreadyExist)
        }
    }
}

@Composable
fun FolderInput(inputText: MutableState<String>, folderAlreadyExist: Boolean) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Input(
            trailingClickableIcon = inputText.value.ifBlank { null }?.let {
                ClickableIcon(
                    icon = Icons.Cancel,
                    onClickTextConsumer = {
                        inputText.value = ""
                    }
                )
            },
            label = {
                {
                    TextBodyS(
                        text = "Folder name",
                        color = if (inputText.value.isBlank()) Base40 else Green50,
                    )
                }
            },
            inputText = inputText,
        )
        if (folderAlreadyExist) {
            TextBodyS(
                modifier = Modifier.fillMaxWidth(),
                text = "Folder already created!",
                color = Red50,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectFolderIcon(
    namedSelectedIcon: MutableState<Pair<String, ImageSource>?>,
    folderAlreadyExist: Boolean,
) {
    Column(
        modifier = Modifier
            .alpha(if (folderAlreadyExist) 0.3f else 1.0f)
            .background(
                color = Orange20,
                shape = RoundedCornerShape(28.dp)
            )
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        namedSelectedIcon.value?.let { namedIcon ->
            TextH4(
                modifier = Modifier.fillMaxWidth(),
                text = "Select Folder Icon",
                textAlign = TextAlign.Center,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Image(
                    modifier = Modifier.size(78.dp),
                    imageSource = namedIcon.second
                )
                Box(
                    modifier = Modifier
                        .background(Color.Unspecified)
                        .clickable {
                            if (!folderAlreadyExist) {
                                namedSelectedIcon.value = null
                            }
                        },
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        Base90,
                                        RoundedCornerShape(100.dp)
                                    )
                            )
                        }
                    }
                }
            }
        } ?: run {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(Icons.getAllFolderIconsMap()) { namedImageSource ->
                    Image(
                        modifier = Modifier
                            .size(78.dp)
                            .clickable {
                                if (!folderAlreadyExist) {
                                    namedSelectedIcon.value = namedImageSource
                                }
                            },
                        imageSource = namedImageSource.second,
                    )
                }
            }
        }
    }
}