package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.InfinityFolderApp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.ui.component.FolderBackgroundPicker
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.ClickableIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Input
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.NavBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Page
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextBodyS
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextH4
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBarIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.Image
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.ImageSource
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.toImageSource
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Add
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Cancel
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Done
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Right
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base5
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange20
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Red50

@Composable
fun AddFolder(
    screen: MutableState<Screen>,
    currentFolderId: String,
    folderDao: FolderDao,
    folderToEdit: Folder? = null,
    onEditDone: () -> Unit = {},
) {
    val editing = folderToEdit != null
    val initialIconName = folderToEdit?.iconName
    val initialIconPair = remember(folderToEdit?.id) {
        val name = initialIconName ?: Icons.FOLDER_ICON_DEFAULT
        val source = Icons.getAllFolderIconsMap()
            .firstOrNull { it.first == name }?.second
            ?: R.drawable.infinity_folder_logo.toImageSource()!!
        name to source
    }

    val inputText: MutableState<String> = remember(folderToEdit?.id) {
        mutableStateOf(folderToEdit?.name.orEmpty())
    }
    val selectedNamedIcon: MutableState<Pair<String, ImageSource>> = remember(folderToEdit?.id) {
        mutableStateOf(initialIconPair)
    }
    val context = LocalContext.current
    val backgroundStore = remember {
        (context.applicationContext as InfinityFolderApp).folderBackgroundStore
    }
    val originalBackgroundImages = remember(folderToEdit?.id) {
        folderToEdit?.backgroundImages.orEmpty()
    }
    val selectedBackgroundPreset: MutableState<String?> = remember(folderToEdit?.id) {
        mutableStateOf(
            if (folderToEdit?.backgroundImages?.isNotEmpty() == true) {
                null
            } else {
                folderToEdit?.backgroundName
            },
        )
    }
    val selectedBackgroundImages: MutableState<List<String>> = remember(folderToEdit?.id) {
        mutableStateOf(folderToEdit?.backgroundImages.orEmpty())
    }
    val selectedBackgroundRotate: MutableState<Int?> = remember(folderToEdit?.id) {
        mutableStateOf(folderToEdit?.backgroundRotateSeconds)
    }
    val defaultRotateSeconds by remember {
        (context.applicationContext as InfinityFolderApp)
            .settingsDao.defaultFolderBackgroundRotateSeconds
    }.collectAsState()
    val iconPickerExpanded = remember { mutableStateOf(!editing) }
    val epoch by folderDao.epoch.collectAsState()
    val folderNames = remember(epoch, currentFolderId, folderToEdit?.id) {
        folderDao.getOrCreate(currentFolderId).launcherObjects
            .mapNotNull { it as? Folder }
            .filter { it.id != folderToEdit?.id }
            .map { it.name }
    }
    val trimmed = inputText.value.trim()
    val folderAlreadyExist = trimmed in folderNames
    val nextRotateForSave =
        if (selectedBackgroundImages.value.size >= 2) selectedBackgroundRotate.value else null
    val canSave = trimmed.isNotBlank() && !folderAlreadyExist && (
        !editing ||
            trimmed != folderToEdit.name ||
            selectedNamedIcon.value.first != (folderToEdit.iconName ?: Icons.FOLDER_ICON_DEFAULT) ||
            selectedBackgroundPreset.value != folderToEdit.backgroundName ||
            selectedBackgroundImages.value != folderToEdit.backgroundImages ||
            nextRotateForSave != folderToEdit.backgroundRotateSeconds
        )

    fun dismiss() {
        if (editing) onEditDone()
        screen.value = Screen.Main
    }

    BackHandler(enabled = iconPickerExpanded.value) {
        iconPickerExpanded.value = false
    }
    BackHandler(enabled = editing) {
        dismiss()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Base5)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        TopBar(
            label = stringResource(
                if (editing) R.string.edit_folder else R.string.add_folder,
            ),
            leftIcon = if (editing) {
                TopBarIcon(Icons.Cancel) { dismiss() }
            } else {
                null
            },
            secondRightIcon = TopBarIcon(
                icon = if (editing) Icons.Done else Icons.Add,
                color = if (editing) Green50 else Base70,
                enabled = canSave,
            ) {
                val iconName = selectedNamedIcon.value.first
                    .takeIf { it != Icons.FOLDER_ICON_DEFAULT }
                val backgroundImages = selectedBackgroundImages.value
                val backgroundName =
                    if (backgroundImages.isNotEmpty()) null else selectedBackgroundPreset.value
                if (editing) {
                    if (
                        folderDao.updateFolder(
                            folderId = folderToEdit!!.id,
                            newName = trimmed,
                            iconName = iconName,
                            backgroundName = backgroundName,
                            backgroundImages = backgroundImages,
                            backgroundRotateSeconds = nextRotateForSave,
                        ) == FolderDao.RenameResult.Ok
                    ) {
                        dismiss()
                    }
                } else {
                    folderDao.createChildFolder(
                        parentId = currentFolderId,
                        name = trimmed,
                        iconName = iconName,
                        backgroundName = backgroundName,
                        backgroundImages = backgroundImages,
                        backgroundRotateSeconds = nextRotateForSave,
                    )
                    screen.value = Screen.Main
                }
            },
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            FolderInput(inputText, folderAlreadyExist)
            SelectFolderIcon(
                namedSelectedIcon = selectedNamedIcon,
                expanded = iconPickerExpanded,
                folderAlreadyExist = folderAlreadyExist,
            )
            FolderBackgroundPicker(
                selectedPresetId = selectedBackgroundPreset.value,
                selectedImageFiles = selectedBackgroundImages.value,
                selectedRotateSeconds = selectedBackgroundRotate.value ?: defaultRotateSeconds,
                onSelectPreset = { id ->
                    backgroundStore.deleteAll(
                        selectedBackgroundImages.value.filter { it !in originalBackgroundImages },
                    )
                    selectedBackgroundPreset.value = id
                    selectedBackgroundImages.value = emptyList()
                },
                onSelectImages = { files ->
                    backgroundStore.deleteAll(
                        selectedBackgroundImages.value.filter { it !in originalBackgroundImages },
                    )
                    selectedBackgroundImages.value = files
                    selectedBackgroundPreset.value = null
                },
                onSelectRotateSeconds = { seconds ->
                    selectedBackgroundRotate.value = seconds
                },
                onSelectUseDefault = {
                    backgroundStore.deleteAll(
                        selectedBackgroundImages.value.filter { it !in originalBackgroundImages },
                    )
                    selectedBackgroundPreset.value = null
                    selectedBackgroundImages.value = emptyList()
                    selectedBackgroundRotate.value = null
                },
                includeUseDefault = true,
            )
        }
        if (!editing) {
            NavBar(Page.Folder, screen)
        }
    }
}

@Composable
fun FolderInput(inputText: MutableState<String>, folderAlreadyExist: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Input(
            trailingClickableIcon = inputText.value.ifBlank { null }?.let {
                ClickableIcon(
                    icon = Icons.Cancel,
                    onClickTextConsumer = { inputText.value = "" },
                )
            },
            label = {
                {
                    TextBodyS(
                        text = stringResource(R.string.folder_name),
                        color = if (inputText.value.isBlank()) Base40 else Green50,
                    )
                }
            },
            inputText = inputText,
        )
        if (folderAlreadyExist) {
            TextBodyS(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.folder_exists),
                color = Red50,
            )
        }
    }
}

@Composable
fun SelectFolderIcon(
    namedSelectedIcon: MutableState<Pair<String, ImageSource>>,
    expanded: MutableState<Boolean>,
    folderAlreadyExist: Boolean,
) {
    Column(
        modifier = Modifier
            .alpha(if (folderAlreadyExist) 0.3f else 1.0f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Orange20)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TextH4(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.select_folder_icon),
            textAlign = TextAlign.Center,
        )

        if (!expanded.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !folderAlreadyExist) {
                        expanded.value = true
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Image(
                    modifier = Modifier
                        .size(78.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    imageSource = namedSelectedIcon.value.second,
                )
                Icon(
                    imageVector = Icons.Right,
                    contentDescription = null,
                    tint = Base70,
                    modifier = Modifier.size(28.dp),
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(Icons.getAllFolderIconsMap()) { namedImageSource ->
                    val selected = namedSelectedIcon.value.first == namedImageSource.first
                    Box(
                        modifier = Modifier
                            .size(78.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .then(
                                if (selected) {
                                    Modifier.border(3.dp, Green50, RoundedCornerShape(16.dp))
                                } else {
                                    Modifier
                                },
                            )
                            .clickable(enabled = !folderAlreadyExist) {
                                namedSelectedIcon.value = namedImageSource
                                expanded.value = false
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            modifier = Modifier.size(78.dp),
                            imageSource = namedImageSource.second,
                        )
                    }
                }
            }
        }
    }
}
