package maks.molch.dmitr.infinityfolderlauncher.ui.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.Image
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.DefaultFontFamily

@Composable
fun Input(
    clickableIcon: ClickableIcon? = null,
) {
    val input = remember { mutableStateOf("") }
    TextField(
        modifier = Modifier.height(64.dp)
            .background(Color.Unspecified),
        textStyle = TextStyle(
            fontFamily = DefaultFontFamily,
            fontSize = 14.sp
        ),
        value = input.value,
        onValueChange = { value -> input.value = value },
        label = {
            Text(
                text = "Folder name",
                color = Base40,
                fontFamily = DefaultFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
        },
        trailingIcon = {
            clickableIcon?.let {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { it.onClickTextConsumer(input.value) },
                    imageVector = it.icon
                )
            }
        }
    )
}

data class ClickableIcon(
    val icon: ImageVector,
    val onClickTextConsumer: (String) -> Unit,
)