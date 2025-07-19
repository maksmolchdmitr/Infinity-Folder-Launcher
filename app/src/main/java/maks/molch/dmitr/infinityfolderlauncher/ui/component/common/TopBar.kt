package maks.molch.dmitr.infinityfolderlauncher.ui.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.DefaultFontFamily

@Composable
fun TopBar(
    label: String,
    leftIcon: TopBarIcon? = null,
    firstRightIcon: TopBarIcon? = null,
    secondRightIcon: TopBarIcon? = null,
) {
    Row(
        modifier = Modifier
            .background(Base0)
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        leftIcon?.let { TopBarIconComposable(it) }
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            textAlign = TextAlign.Left,
            fontSize = 20.sp,
            fontFamily = DefaultFontFamily,
        )
        firstRightIcon?.let { TopBarIconComposable(it) }
        secondRightIcon?.let { TopBarIconComposable(it) }
    }
}

@Composable
fun TopBarIconComposable(icon: TopBarIcon) {
    Icon(
        modifier = Modifier
            .alpha(if (icon.enabled) 1f else 0.3f)
            .size(28.dp)
            .clickable { if (icon.enabled) icon.onClick() },
        imageVector = icon.icon,
        contentDescription = null,
        tint = icon.color,
    )
}

data class TopBarIcon(
    val icon: ImageVector,
    val color: Color = Base70,
    val enabled: Boolean = true,
    val onClick: () -> Unit,
)