package maks.molch.dmitr.infinityfolderlauncher.ui.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.Image
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.ImageSource
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange10
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Red50

@Composable
fun ConfirmRemove(
    mainIcon: ImageSource? = null,
    mainText: String,
    descriptionText: String,
    removeText: String,
    onCancelClick: () -> Unit,
    onRemoveClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(color = Orange10, shape = RoundedCornerShape(28.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            mainIcon?.let {
                Image(
                    imageSource = it,
                    modifier = Modifier.size(78.dp)
                )
            }
            TextH4(
                text = mainText,
                textAlign = TextAlign.Center,
            )
        }
        TextBodyS(
            text = descriptionText,
            color = Red50,
        )
        Row {
            IconButton(
                modifier = Modifier
                    .width(75.dp)
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .background(
                        color = Base50,
                        shape = RoundedCornerShape(12.dp)
                    ),
                onClick = onCancelClick,
            ) {
                TextBodyS(
                    text = "Cancel",
                    color = Base0,
                    fontWeight = FontWeight.Medium,
                )
            }
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .background(
                        color = Red50,
                        shape = RoundedCornerShape(12.dp)
                    ),
                onClick = onRemoveClick,
            ) {
                TextBodyS(
                    text = removeText,
                    color = Base0,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}