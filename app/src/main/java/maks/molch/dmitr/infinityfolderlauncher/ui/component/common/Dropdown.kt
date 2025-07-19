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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.Image
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.ImageSource
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base5
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.DefaultFontFamily

@Composable
fun Dropdown(dropdownItems: List<DropdownItem>) {
    if (dropdownItems.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .background(
                    color = Base5,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp,
                    )
                )
                .padding(vertical = 12.dp),
        ) {
            items(dropdownItems) { DropdownRow(dropdownItem = it) }
        }
    } else {
        Text(
            modifier = Modifier
                .background(Color.Unspecified, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
                .fillMaxWidth(),
            text = "Empty query result!",
            fontFamily = DefaultFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
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
            imageSource = dropdownItem.icon,
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
    val icon: ImageSource,
    val name: String,
    val onClick: (() -> Unit)? = null,
) {
    constructor(icon: Any, onClick: () -> Unit, name: String) : this(
        ImageSource.from(icon) ?: ImageSource.Resource(R.drawable.infinity_folder_logo),
        name,
        onClick,
    )
}