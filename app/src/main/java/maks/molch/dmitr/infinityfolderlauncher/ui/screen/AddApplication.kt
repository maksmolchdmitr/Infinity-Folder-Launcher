package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.dao.ApplicationDao
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.data.Application
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCell
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCellState
import maks.molch.dmitr.infinityfolderlauncher.ui.component.calcState
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.ClickableIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Input
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.NavBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Page
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextBodyS
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBarIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Add
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Search
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddApplication(
    context: Context,
    screen: MutableState<Screen>,
    applicationDao: ApplicationDao,
    currentFolderName: String,
    folderDao: FolderDao
) {
    val objectNumberOnTheRow = 4

    val multipleChoiceEnabled: MutableState<Boolean> = remember { mutableStateOf(false) }
    val selectedObjects: MutableState<Set<LauncherObject>> = remember {
        mutableStateOf(
            setOf()
        )
    }
    val query: MutableState<String?> = remember { mutableStateOf(null) }

    val allApplications: List<Application> = applicationDao.getInstalledApplications()
        .filter { application ->
            query.value?.let { application.name.lowercase().contains(it.lowercase()) } ?: true
        }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(
            label = "Add App",
            secondRightIcon = TopBarIcon(
                icon = Icons.Add,
                color = Base70,
                enabled = selectedObjects.value.isNotEmpty(),
            ) {
                folderDao.addObjectsAndSave(currentFolderName, selectedObjects.value)
                screen.value = Screen.Main
            }
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
                .combinedClickable(
                    onLongClick = {
                        multipleChoiceEnabled.value = !multipleChoiceEnabled.value
                    },
                    onClick = {}
                )
                .paint(
                    painter = painterResource(R.drawable.infinity_folder_logo),
                    contentScale = ContentScale.Crop,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Input(
                trailingClickableIcon = ClickableIcon(
                    icon = Icons.Search,
                    onClickTextConsumer = { text ->
                        query.value = text.ifBlank { null }
                    }
                ),
                label = { text ->
                    {
                        TextBodyS(
                            text = "App name",
                            color = if (text.isBlank()) Base40 else Green50,
                        )
                    }
                },
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(objectNumberOnTheRow),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                items(allApplications) { application ->
                    ObjectCell(
                        context,
                        application,
                        multipleChoiceEnabled,
                        selectedObjects,
                    ) {
                        if (multipleChoiceEnabled.value) {
                            val state: ObjectCellState = calcState(selectedObjects, application)
                            if (state == ObjectCellState.SelectionBlank) {
                                selectedObjects.value += application
                            } else {
                                selectedObjects.value = selectedObjects.value
                                    .filter { it.name != application.name }
                                    .toSet()
                            }

                            return@ObjectCell
                        }

                        folderDao.addObjectsAndSave(currentFolderName, setOf(application))
                        screen.value = Screen.Main
                    }
                }
            }
        }
        NavBar(Page.AddApplication, context, screen)
    }
}