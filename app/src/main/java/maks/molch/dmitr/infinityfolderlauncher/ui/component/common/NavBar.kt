package maks.molch.dmitr.infinityfolderlauncher.ui.component.common

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Add
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.FolderMultiple
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Home
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Widgets
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base10
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.utils.toastMakeTextAndShow

@Composable
fun NavBar(currentState: Page, context: Context, screen: MutableState<Screen>) {
    val iconMap = mapOf(
        Page.Home to Icons.Home,
        Page.AddApplication to Icons.Add,
        Page.Folder to Icons.FolderMultiple,
        Page.Widget to Icons.Widgets,
    )
    Row(
        modifier = Modifier
            .height(56.dp)
    ) {
        Page.entries.forEach { page ->
            Navigation(
                this,
                iconMap[page]!!,
                currentState == page,
            ) {
                context.toastMakeTextAndShow("${page.name} nav bar")
                screen.value = when (page) {
                    Page.Home -> Screen.Main
                    Page.AddApplication -> Screen.AddApplication
                    Page.Folder -> TODO()
                    Page.Widget -> TODO()
                }
            }
        }
    }
}

@Composable
fun Navigation(
    rowScope: RowScope,
    icon: ImageVector,
    active: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor: Color = if (active) Base10 else Base0
    val iconColor: Color = if (active) Green50 else Base50
    rowScope.apply {
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { onClick() }
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
            )
        }
    }
}

enum class Page {
    Home,
    AddApplication,
    Folder,
    Widget,
}