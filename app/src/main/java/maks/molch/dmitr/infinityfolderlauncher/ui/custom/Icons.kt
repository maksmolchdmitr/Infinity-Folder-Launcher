package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.vector.ImageVector
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Default
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Education
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Games
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Internet
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Other
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Shop
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Social
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Work

object Icons {
    fun folderIconByName(name: String): ImageVector? {
        return when (name) {
            "Default" -> Icons.Default
            "Education" -> Icons.Education
            "Games" -> Icons.Games
            "Internet" -> Icons.Internet
            "Other" -> Icons.Other
            "Shop" -> Icons.Shop
            "Social" -> Icons.Social
            "Work" -> Icons.Work
            else -> null
        }
    }
}