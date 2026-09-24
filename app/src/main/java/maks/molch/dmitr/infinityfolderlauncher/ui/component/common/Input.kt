package maks.molch.dmitr.infinityfolderlauncher.ui.component.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.Image
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.ImageSource
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.DefaultFontStyle
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50

@Composable
fun Input(
    leadingClickableIcon: ClickableIcon? = null,
    trailingClickableIcon: ClickableIcon? = null,
    label: (String) -> @Composable () -> Unit = { { } },
    inputText: MutableState<String>? = null,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
) {
    val input: MutableState<String> = inputText ?: remember { mutableStateOf("") }
    val leadingIcon: @Composable (() -> Unit)? = leadingClickableIcon?.let {
        {
            it.icon?.let { imageSource ->
                Image(
                    imageSource = imageSource,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { it.onClickTextConsumer?.invoke(input.value) },
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
                        .clickable { it.onClickTextConsumer?.invoke(input.value) },
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
        value = input.value,
        onValueChange = { value -> input.value = value },
        label = label.invoke(input.value),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Base0,
            unfocusedContainerColor = Base0,
            disabledContainerColor = Base0,
            focusedBorderColor = Green50,
            unfocusedBorderColor = Base40,
        ),
    )
}

data class ClickableIcon(
    val icon: ImageSource?,
    val onClickTextConsumer: ((String) -> Unit)? = null,
) {
    constructor(
        icon: Any,
        onClickTextConsumer: ((String) -> Unit)? = null,
    ) : this(
        ImageSource.from(icon),
        onClickTextConsumer,
    )
}
