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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.R
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
    currentFolderId: String,
    selectedObjects: MutableState<Set<LauncherObject>>,
    moveObjectsEnabled: MutableState<Boolean>,
    editModeEnabled: MutableState<Boolean>,
) {
    val selectedFolder: MutableState<Folder?> = remember { mutableStateOf(null) }
    val blockedIds = remember(selectedObjects.value) {
        selectedObjects.value
            .filterIsInstance<Folder>()
            .flatMap { folderDao.collectDescendantFolderIds(it.id) }
            .toSet() + currentFolderId
    }

    Column(
        modifier = Modifier
            .width(328.dp)
            .background(color = Orange10, shape = RoundedCornerShape(28.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.choose_target_folder),
            fontFamily = DefaultFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Base100,
        )
        FolderSearch(
            folderDao = folderDao,
            selectedFolder = selectedFolder,
            excludeFolderIds = blockedIds,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(31.dp)) {
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
                    text = stringResource(R.string.cancel),
                    fontFamily = DefaultFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Base0,
                )
            }
            IconButton(
                onClick = {
                    selectedFolder.value?.let { folder ->
                        val allowed = selectedObjects.value.filter { obj ->
                            obj !is Folder || !folderDao.wouldCreateCycle(obj.id, folder.id)
                        }.toSet()
                        if (allowed.isNotEmpty()) {
                            folderDao.addObjectsAndSave(folder.id, allowed)
                            folderDao.removeObjectsAndSave(currentFolderId, allowed)
                        }
                        selectedObjects.value = setOf()
                        moveObjectsEnabled.value = false
                        editModeEnabled.value = false
                    }
                },
                modifier = Modifier
                    .background(
                        color = if (selectedFolder.value == null) Base20 else Green50,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.move),
                    fontFamily = DefaultFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Base0,
                )
            }
        }
    }
}
