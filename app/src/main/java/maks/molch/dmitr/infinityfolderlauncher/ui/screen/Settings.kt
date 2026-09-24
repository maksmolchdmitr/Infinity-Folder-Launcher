package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.dao.SettingsDao
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextBodyS
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextH4
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBarIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Cancel
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base5
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange20

@Composable
fun SettingsScreen(
    screen: MutableState<Screen>,
    settingsDao: SettingsDao,
) {
    val mainColumns by settingsDao.mainColumns.collectAsState()
    val searchColumns by settingsDao.searchColumns.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Base5)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        TopBar(
            label = stringResource(R.string.settings),
            secondRightIcon = TopBarIcon(Icons.Cancel) {
                screen.value = Screen.Main
            },
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ColumnsCard(
                title = stringResource(R.string.settings_main_columns),
                value = mainColumns,
                onMinus = { settingsDao.setMainColumns(mainColumns - 1) },
                onPlus = { settingsDao.setMainColumns(mainColumns + 1) },
            )
            ColumnsCard(
                title = stringResource(R.string.settings_search_columns),
                value = searchColumns,
                onMinus = { settingsDao.setSearchColumns(searchColumns - 1) },
                onPlus = { settingsDao.setSearchColumns(searchColumns + 1) },
            )
        }
    }
}

@Composable
private fun ColumnsCard(
    title: String,
    value: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Orange20, RoundedCornerShape(28.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TextH4(text = title)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = onMinus,
                modifier = Modifier.background(Base70, RoundedCornerShape(12.dp)),
            ) {
                TextBodyS(text = "−", color = Base0, fontWeight = FontWeight.Bold)
            }
            TextH4(text = value.toString())
            IconButton(
                onClick = onPlus,
                modifier = Modifier.background(Green50, RoundedCornerShape(12.dp)),
            ) {
                TextBodyS(text = "+", color = Base0, fontWeight = FontWeight.Bold)
            }
        }
    }
}
