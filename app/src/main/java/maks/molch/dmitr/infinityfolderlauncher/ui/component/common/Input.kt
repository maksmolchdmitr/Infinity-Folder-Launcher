package maks.molch.dmitr.infinityfolderlauncher.ui.component.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.Image
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.ImageSource
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.DefaultFontFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Input(
    leadingClickableIcon: ClickableIcon? = null,
    trailingClickableIcon: ClickableIcon? = null,
    textColor: Color = Base40,
    inputText: String? = null
) {
    val input = remember { mutableStateOf("") }
    val leadingIcon: @Composable (() -> Unit)? = leadingClickableIcon?.let {
        {
            it.icon?.let { imageSource ->
                Image(
                    imageSource = imageSource,
                    modifier = Modifier
                        .size(24.dp)
                        .combinedClickable(
                            onClick = { it.onClickTextConsumer?.invoke(input.value) },
                            onLongClick = { it.onLongClickConsumer?.invoke(input.value) }
                        ),
                )
            }
        }
    }
    val trailingIcon: @Composable (() -> Unit)? = trailingClickableIcon?.let {
        {
            it.icon?.let { imageSource ->
                Image(
                    imageSource = imageSource,
                    modifier = Modifier
                        .size(24.dp)
                        .combinedClickable(
                            onClick = { it.onClickTextConsumer?.invoke(input.value) },
                            onLongClick = { it.onLongClickConsumer?.invoke(input.value) }
                        )
                )
            }
        }
    }
    TextField(
        modifier = Modifier
            .height(64.dp),
        textStyle = TextStyle(
            fontFamily = DefaultFontFamily,
            fontSize = 14.sp
        ),
        value = inputText ?: input.value,
        onValueChange = { value -> input.value = value },
        label = {
            Text(
                text = "Folder name",
                color = textColor,
                fontFamily = DefaultFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
    )
}

data class ClickableIcon(
    val icon: ImageSource?,
    val onClickTextConsumer: ((String) -> Unit)? = null,
    val onLongClickConsumer: ((String) -> Unit)? = null,
) {
    constructor(
        icon: Any,
        onClickTextConsumer: ((String) -> Unit)? = null,
        onLongClickConsumer: ((String) -> Unit)? = null,
    ) : this(
        ImageSource.from(icon),
        onClickTextConsumer,
        onLongClickConsumer,
    )
}