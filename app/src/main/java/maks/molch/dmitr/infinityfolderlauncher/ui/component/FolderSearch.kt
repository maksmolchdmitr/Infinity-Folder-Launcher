package maks.molch.dmitr.infinityfolderlauncher.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.ClickableIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Dropdown
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.DropdownItem
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Input
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.FolderSearch
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Settings
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base20

@Composable
fun FolderSearch() {
    val dropdownOn = remember { mutableStateOf(false) }
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
                    dropdownOn.value = !dropdownOn.value
                }
            )
        )
        if (dropdownOn.value) {
            Dropdown(
                List(25) { index ->
                    DropdownItem(
                        icon = Icons.Settings,
                        name = "Settings $index",
                    )
                }
            )
        }
    }
}