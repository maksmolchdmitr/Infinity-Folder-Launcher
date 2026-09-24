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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange10
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Red50

@Composable
fun AppRemoveDialog(
    appName: String,
    onCancel: () -> Unit,
    onRemoveFromFolder: () -> Unit,
    onUninstall: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(328.dp)
            .background(Orange10, RoundedCornerShape(28.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TextH4(text = appName)
        TextBodyS(text = stringResource(R.string.uninstall_confirm))
        IconButton(
            onClick = onRemoveFromFolder,
            modifier = Modifier
                .fillMaxWidth()
                .background(Green50, RoundedCornerShape(12.dp)),
        ) {
            TextBodyS(
                text = stringResource(R.string.remove_from_folder),
                color = Base0,
                fontWeight = FontWeight.Medium,
            )
        }
        IconButton(
            onClick = onUninstall,
            modifier = Modifier
                .fillMaxWidth()
                .background(Red50, RoundedCornerShape(12.dp)),
        ) {
            TextBodyS(
                text = stringResource(R.string.uninstall_app),
                color = Base0,
                fontWeight = FontWeight.Medium,
            )
        }
        IconButton(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .background(Base50, RoundedCornerShape(12.dp)),
        ) {
            TextBodyS(
                text = stringResource(R.string.cancel),
                color = Base0,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}
