package maks.molch.dmitr.infinityfolderlauncher.ui.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.Image
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base20
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.DefaultFontFamily

@Composable
fun Dropdown(dropdownItems: List<DropdownItem>) {
    LazyColumn(
        modifier = Modifier
            .background(color = Base20)
            .padding(vertical = 12.dp),
    ) {
        items(dropdownItems) { DropdownRow(dropdownItem = it) }
    }
}

@Composable
fun DropdownRow(dropdownItem: DropdownItem) {
    Row(
        modifier = Modifier
            .height(71.dp)
            .background(color = Base0)
            .fillMaxWidth()
            .clickable { dropdownItem.onClick?.invoke() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Image(
            imageVector = dropdownItem.icon,
            modifier = Modifier.size(39.dp)
        )
        Text(
            modifier = Modifier.weight(1f),
            text = dropdownItem.name,
            fontFamily = DefaultFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}

data class DropdownItem(
    val icon: ImageVector,
    val name: String,
    val onClick: (() -> Unit)? = null,
)