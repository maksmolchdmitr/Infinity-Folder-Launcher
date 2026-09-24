package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.WebsiteShortcut
import maks.molch.dmitr.infinityfolderlauncher.data.normalizeWebsiteUrl
import maks.molch.dmitr.infinityfolderlauncher.ui.component.FolderSearch
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.ClickableIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Input
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.NavBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Page
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextBodyS
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextH4
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBarIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Add
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Cancel
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base5
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.DefaultFontFamily
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange20
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Red50

@Composable
fun AddWidgetScreen(
    screen: MutableState<Screen>,
    currentFolderId: String,
    folderDao: FolderDao,
) {
    val mode = remember { mutableStateOf<WidgetMode>(WidgetMode.Chooser) }

    when (mode.value) {
        WidgetMode.Chooser -> Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Base5)
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            TopBar(
                label = stringResource(R.string.add_widget),
                secondRightIcon = TopBarIcon(Icons.Cancel) {
                    screen.value = Screen.Main
                },
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                WidgetOptionCard(
                    title = stringResource(R.string.website_shortcut),
                    subtitle = stringResource(R.string.website_shortcut_hint),
                    onClick = { mode.value = WidgetMode.Website },
                )
                WidgetOptionCard(
                    title = stringResource(R.string.system_widgets_soon),
                    subtitle = null,
                    enabled = false,
                    onClick = {},
                )
            }
            NavBar(Page.Widget, screen)
        }

        WidgetMode.Website -> AddWebsiteShortcut(
            screen = screen,
            currentFolderId = currentFolderId,
            folderDao = folderDao,
            onBack = { mode.value = WidgetMode.Chooser },
        )
    }
}

private enum class WidgetMode { Chooser, Website }

@Composable
private fun WidgetOptionCard(
    title: String,
    subtitle: String?,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Orange20, RoundedCornerShape(28.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            fontFamily = DefaultFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = if (enabled) Base70 else Base40,
        )
        subtitle?.let {
            TextBodyS(text = it, color = Base40)
        }
    }
}

@Composable
private fun AddWebsiteShortcut(
    screen: MutableState<Screen>,
    currentFolderId: String,
    folderDao: FolderDao,
    onBack: () -> Unit,
) {
    val title = remember { mutableStateOf("") }
    val url = remember { mutableStateOf("") }
    val selectedFolder: MutableState<Folder?> = remember {
        mutableStateOf(folderDao.getById(currentFolderId))
    }
    val normalized = normalizeWebsiteUrl(url.value)
    val valid = title.value.isNotBlank() &&
        normalized.contains('.') &&
        normalized.length > 8 &&
        selectedFolder.value != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Base5)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        TopBar(
            label = stringResource(R.string.website_shortcut),
            leftIcon = TopBarIcon(Icons.Cancel, onClick = onBack),
            secondRightIcon = TopBarIcon(
                icon = Icons.Add,
                color = Base70,
                enabled = valid,
            ) {
                val targetId = selectedFolder.value?.id ?: currentFolderId
                folderDao.addObjectsAndSave(
                    targetId,
                    setOf(
                        WebsiteShortcut(
                            name = title.value.trim(),
                            url = normalized,
                        ),
                    ),
                )
                screen.value = Screen.Main
            },
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Input(
                trailingClickableIcon = title.value.ifBlank { null }?.let {
                    ClickableIcon(
                        icon = Icons.Cancel,
                        onClickTextConsumer = { title.value = "" },
                    )
                },
                label = {
                    {
                        TextBodyS(
                            text = stringResource(R.string.site_title),
                            color = if (title.value.isBlank()) Base40 else Green50,
                        )
                    }
                },
                inputText = title,
            )
            Input(
                trailingClickableIcon = url.value.ifBlank { null }?.let {
                    ClickableIcon(
                        icon = Icons.Cancel,
                        onClickTextConsumer = { url.value = "" },
                    )
                },
                label = {
                    {
                        TextBodyS(
                            text = stringResource(R.string.site_url),
                            color = if (url.value.isBlank()) Base40 else Green50,
                        )
                    }
                },
                inputText = url,
            )
            if (url.value.isNotBlank() && !(normalized.contains('.') && normalized.length > 8)) {
                TextBodyS(text = stringResource(R.string.invalid_url), color = Red50)
            }
            TextH4(text = stringResource(R.string.target_folder))
            FolderSearch(
                folderDao = folderDao,
                selectedFolder = selectedFolder,
            )
        }
        NavBar(Page.Widget, screen)
    }
}
