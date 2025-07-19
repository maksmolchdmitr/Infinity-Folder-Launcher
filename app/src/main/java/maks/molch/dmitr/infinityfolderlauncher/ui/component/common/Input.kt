package maks.molch.dmitr.infinityfolderlauncher.ui.component.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.Image
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.ImageSource
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.DefaultFontStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Input(
    leadingClickableIcon: ClickableIcon? = null,
    trailingClickableIcon: ClickableIcon? = null,
    label: (String) -> @Composable () -> Unit = { { } },
    inputText: String? = null,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
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
    OutlinedTextField(
        shape = shape,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        textStyle = DefaultFontStyle,
        value = inputText ?: input.value,
        onValueChange = { value -> input.value = value },
        label = label.invoke(input.value),
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