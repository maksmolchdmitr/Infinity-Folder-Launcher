package maks.molch.dmitr.infinityfolderlauncher.ui.custom

import androidx.compose.ui.graphics.vector.ImageVector
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.ImageSource
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Education
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Finance
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Games
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Internet
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Other
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Shop
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Social
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.folder.Work

object Icons {
    const val FOLDER_ICON_DEFAULT = "Default"
    const val FOLDER_ICON_APPS_PREVIEW = "AppsPreview"

    private val IconsMap: Map<String, ImageVector> = mapOf(
        "Education" to Icons.Education,
        "Finance" to Icons.Finance,
        "Games" to Icons.Games,
        "Internet" to Icons.Internet,
        "Other" to Icons.Other,
        "Shop" to Icons.Shop,
        "Social" to Icons.Social,
        "Work" to Icons.Work,
    )

    fun folderIconByName(name: String): ImageVector? = when (name) {
        FOLDER_ICON_APPS_PREVIEW -> Icons.FolderMultiple
        else -> IconsMap[name]
    }

    fun getAllFolderIconsMap(): List<Pair<String, ImageSource>> =
        listOfNotNull(
            ImageSource.from(R.drawable.infinity_folder_logo)?.let { FOLDER_ICON_DEFAULT to it },
            ImageSource.from(Icons.FolderMultiple)?.let { FOLDER_ICON_APPS_PREVIEW to it },
        ) + IconsMap.entries.mapNotNull { (name, vector) ->
            ImageSource.from(vector)?.let { name to it }
        }
}