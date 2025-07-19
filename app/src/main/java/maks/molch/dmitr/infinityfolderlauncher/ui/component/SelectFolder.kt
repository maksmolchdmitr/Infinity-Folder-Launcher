package maks.molch.dmitr.infinityfolderlauncher.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base100
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base20
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.DefaultFontFamily
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange10

@Composable
fun SelectFolder(
    folderDao: FolderDao,
    currentFolderName: String,
    selectedObjects: MutableState<Set<LauncherObject>>,
    moveObjectsEnabled: MutableState<Boolean>,
    editModeEnabled: MutableState<Boolean>
) {
    val selectedFolder: MutableState<Folder?> = remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .width(328.dp)
            .background(color = Orange10, shape = RoundedCornerShape(28.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Choose target folder for moving objects",
            fontFamily = DefaultFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Base100,
        )
        FolderSearch(folderDao, selectedFolder)
        Row(
            horizontalArrangement = Arrangement.spacedBy(31.dp)
        ) {
            IconButton(
                onClick = {
                    moveObjectsEnabled.value = false
                    editModeEnabled.value = false
                },
                modifier = Modifier
                    .width(89.dp)
                    .background(color = Base50, shape = RoundedCornerShape(12.dp)),
            ) {
                Text(
                    text = "Cancel",
                    fontFamily = DefaultFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Base0,
                )
            }
            IconButton(
                onClick = {
                    selectedFolder.value?.let { folder ->
                        onMoveClick(
                            folder.name,
                            currentFolderName,
                            folderDao,
                            selectedObjects,
                            moveObjectsEnabled,
                            editModeEnabled,
                        )
                    }
                },
                modifier = Modifier
                    .background(
                        color = if (selectedFolder.value == null) Base20 else Green50,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .weight(1f),
            ) {
                Text(
                    text = "Move",
                    fontFamily = DefaultFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Base0,
                )
            }
        }
    }
}

private fun onMoveClick(
    folderName: String,
    currentFolderName: String,
    folderDao: FolderDao,
    selectedObjects: MutableState<Set<LauncherObject>>,
    moveObjectsEnabled: MutableState<Boolean>,
    editModeEnabled: MutableState<Boolean>,
) {
    folderDao.addObjectsAndSave(folderName, selectedObjects.value)
    folderDao.removeObjectsAndSave(currentFolderName, selectedObjects.value)
    moveObjectsEnabled.value = false
    editModeEnabled.value = false
}