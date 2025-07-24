package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.vector.ImageVector
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.ImageSource
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Education
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Games
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Internet
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Other
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Shop
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Social
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Work

object Icons {
    private val IconsMap: Map<String, ImageVector> = mapOf(
        "Education" to Icons.Education,
        "Games" to Icons.Games,
        "Internet" to Icons.Internet,
        "Other" to Icons.Other,
        "Shop" to Icons.Shop,
        "Social" to Icons.Social,
        "Work" to Icons.Work,
    )

    fun folderIconByName(name: String): ImageVector? = IconsMap[name]

    fun getAllFolderIconsMap(): List<Pair<String, ImageSource>> =
        (listOf("Default" to R.drawable.infinity_folder_logo) +
                IconsMap.entries.map { it.key to it.value })
            .mapNotNull { pair ->
                ImageSource.from(pair.second)?.let { pair.first to it }
            }
}