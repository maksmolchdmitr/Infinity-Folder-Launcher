package maks.molch.dmitr.infinityfolderlauncher.ui.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Cancel
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange10
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Red50

@Composable
fun RenameFolderDialog(
    initialName: String,
    nameTaken: (String) -> Boolean,
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    val inputText: MutableState<String> = remember { mutableStateOf(initialName) }
    val trimmed = inputText.value.trim()
    val taken = trimmed.isNotEmpty() && trimmed != initialName && nameTaken(trimmed)
    val canSave = trimmed.isNotBlank() && !taken && trimmed != initialName

    Column(
        modifier = Modifier
            .width(328.dp)
            .background(color = Orange10, shape = RoundedCornerShape(28.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TextH4(text = stringResource(R.string.rename_folder))
        Input(
            trailingClickableIcon = inputText.value.ifBlank { null }?.let {
                ClickableIcon(
                    icon = Icons.Cancel,
                    onClickTextConsumer = { inputText.value = "" },
                )
            },
            label = {
                {
                    TextBodyS(
                        text = stringResource(R.string.folder_name),
                        color = if (inputText.value.isBlank()) Base40 else Green50,
                    )
                }
            },
            inputText = inputText,
        )
        if (taken) {
            TextBodyS(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.folder_exists),
                color = Red50,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(
                onClick = onCancel,
                modifier = Modifier
                    .width(89.dp)
                    .background(color = Base50, shape = RoundedCornerShape(12.dp)),
            ) {
                TextBodyS(
                    text = stringResource(R.string.cancel),
                    color = Base0,
                    fontWeight = FontWeight.Medium,
                )
            }
            IconButton(
                onClick = { if (canSave) onConfirm(trimmed) },
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = if (canSave) Green50 else Base50,
                        shape = RoundedCornerShape(12.dp),
                    ),
            ) {
                TextBodyS(
                    text = stringResource(R.string.save),
                    color = Base0,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}
