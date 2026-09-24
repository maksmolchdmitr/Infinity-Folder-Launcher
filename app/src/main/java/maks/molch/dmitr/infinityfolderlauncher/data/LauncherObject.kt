package maks.molch.dmitr.infinityfolderlauncher.data

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import java.util.UUID

sealed class LauncherObject {
    abstract val id: String
    abstract val name: String
}

data class Folder(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    val launcherObjects: List<LauncherObject> = emptyList(),
    val iconName: String? = null,
) : LauncherObject() {
    fun asReference(): Folder = copy(launcherObjects = emptyList())
}

data class Application(
    override val id: String,
    override val name: String,
    val packageName: String,
) : LauncherObject() {
    constructor(
        applicationInfo: ApplicationInfo,
        packageManager: PackageManager,
    ) : this(
        id = applicationInfo.packageName,
        name = applicationInfo.loadLabel(packageManager).toString(),
        packageName = applicationInfo.packageName,
    )

    fun getIcon(packageManager: PackageManager): Drawable =
        packageManager.getApplicationIcon(packageName)
}

data class WebsiteShortcut(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    val url: String,
) : LauncherObject()

fun Folder.getIcon(): Any = iconName?.let { Icons.folderIconByName(it) }
    ?: R.drawable.infinity_folder_logo

fun normalizeWebsiteUrl(raw: String): String {
    val trimmed = raw.trim()
    if (trimmed.isEmpty()) return trimmed
    return if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
        trimmed
    } else {
        "https://$trimmed"
    }
}
