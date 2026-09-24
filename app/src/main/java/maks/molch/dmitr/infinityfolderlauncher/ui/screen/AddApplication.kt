package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.dao.ApplicationDao
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.dao.SettingsDao
import maks.molch.dmitr.infinityfolderlauncher.data.Application
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCell
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCellState
import maks.molch.dmitr.infinityfolderlauncher.ui.component.SelectionStyle
import maks.molch.dmitr.infinityfolderlauncher.ui.component.calcState
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.ClickableIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Input
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.NavBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Page
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextBodyS
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBarIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Cancel
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.CheckboxMarked
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Done
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Search
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base5
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50

@Composable
fun AddApplication(
    context: Context,
    screen: MutableState<Screen>,
    applicationDao: ApplicationDao,
    currentFolderId: String,
    folderDao: FolderDao,
    settingsDao: SettingsDao,
) {
    val columns by settingsDao.searchColumns.collectAsState()
    val epoch by folderDao.epoch.collectAsState()
    val cachedApps by applicationDao.apps.collectAsState()
    val loading by applicationDao.loading.collectAsState()

    val multipleChoiceEnabled: MutableState<Boolean> = remember { mutableStateOf(false) }
    val selectedObjects: MutableState<Set<LauncherObject>> = remember { mutableStateOf(setOf()) }
    val query: MutableState<String> = remember { mutableStateOf("") }

    val existingIds = remember(epoch, currentFolderId) {
        folderDao.getOrCreate(currentFolderId).launcherObjects.map { it.id }.toSet()
    }

    val allApplications: List<Application> = remember(cachedApps, query.value) {
        val q = query.value.trim().lowercase()
        if (q.isEmpty()) {
            cachedApps
        } else {
            cachedApps.filter { it.name.lowercase().contains(q) }
        }
    }

    fun exitMultiSelect() {
        multipleChoiceEnabled.value = false
        selectedObjects.value = setOf()
    }

    fun confirmSelection() {
        if (selectedObjects.value.isEmpty()) return
        folderDao.addObjectsAndSave(currentFolderId, selectedObjects.value)
        exitMultiSelect()
        screen.value = Screen.Main
    }

    BackHandler(enabled = multipleChoiceEnabled.value) {
        exitMultiSelect()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Base5)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        if (multipleChoiceEnabled.value) {
            TopBar(
                label = stringResource(R.string.multi_select_apps),
                leftIcon = TopBarIcon(Icons.Cancel) { exitMultiSelect() },
                secondRightIcon = TopBarIcon(
                    icon = Icons.Done,
                    color = Green50,
                    enabled = selectedObjects.value.isNotEmpty(),
                ) { confirmSelection() },
            )
        } else {
            TopBar(
                label = stringResource(R.string.add_app),
                secondRightIcon = TopBarIcon(Icons.CheckboxMarked) {
                    multipleChoiceEnabled.value = true
                },
            )
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Input(
                trailingClickableIcon = ClickableIcon(
                    icon = Icons.Search,
                    onClickTextConsumer = { text -> query.value = text },
                ),
                label = { text ->
                    {
                        TextBodyS(
                            text = stringResource(R.string.search_apps_hint),
                            color = if (text.isBlank()) Base40 else Green50,
                        )
                    }
                },
                inputText = query,
            )
            when {
                loading && cachedApps.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = Green50,
                        )
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columns),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        items(allApplications, key = { it.id }) { application ->
                            ObjectCell(
                                context = context,
                                launcherObject = application,
                                editModeEnabled = multipleChoiceEnabled,
                                selectedObjects = selectedObjects,
                                selectionStyle = SelectionStyle.Checkbox,
                                onClick = {
                                    if (multipleChoiceEnabled.value) {
                                        val state = calcState(selectedObjects, application)
                                        selectedObjects.value =
                                            if (state == ObjectCellState.SelectionBlank) {
                                                selectedObjects.value + application
                                            } else {
                                                selectedObjects.value
                                                    .filter { it.id != application.id }
                                                    .toSet()
                                            }
                                    } else {
                                        if (application.id !in existingIds) {
                                            folderDao.addObjectsAndSave(
                                                currentFolderId,
                                                setOf(application),
                                            )
                                        }
                                        screen.value = Screen.Main
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
        NavBar(Page.AddApplication, screen)
    }
}
