package maks.molch.dmitr.infinityfolderlauncher.data

import android.content.ComponentName
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
    /** Named preset from FolderBackgrounds; null = use Settings default. */
    val backgroundName: String? = null,
    /** App-private image file names under folder_backgrounds/; wins over [backgroundName]. */
    val backgroundImages: List<String> = emptyList(),
    /** Slideshow period in seconds when [backgroundImages] has 2+ items; null = settings default. */
    val backgroundRotateSeconds: Int? = null,
    val requireDistinctObjects: Boolean = true,
    val deleteContentsOnRemove: Boolean = true,
) : LauncherObject() {
    fun asReference(): Folder = copy(launcherObjects = emptyList())
}

data class Application(
    override val id: String,
    override val name: String,
    val packageName: String,
    /** Launcher activity, when one package exposes several (contacts and phone). */
    val activityName: String? = null,
) : LauncherObject() {
    constructor(
        applicationInfo: ApplicationInfo,
        packageManager: PackageManager,
    ) : this(
        id = applicationInfo.packageName,
        name = applicationInfo.loadLabel(packageManager).toString(),
        packageName = applicationInfo.packageName,
    )

    fun getIcon(packageManager: PackageManager): Drawable {
        val activity = activityName
        if (activity != null) {
            val activityIcon = runCatching {
                packageManager.getActivityInfo(
                    ComponentName(packageName, activity),
                    0,
                ).loadIcon(packageManager)
            }.getOrNull()
            if (activityIcon != null) return activityIcon
        }
        return packageManager.getApplicationIcon(packageName)
    }
}

data class WebsiteShortcut(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    val url: String,
) : LauncherObject()

data class AppWidgetItem(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    val appWidgetId: Int,
    val provider: String,
    val spanCols: Int = 2,
    val spanRows: Int = 2,
) : LauncherObject()

/**
 * Built-in steps counter card (not a system AppWidget) — works on Xiaomi third-party launchers.
 */
data class StepsWidget(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String = "Шаги",
    val dailyGoal: Int = 10_000,
    val spanCols: Int = 2,
    val spanRows: Int = 1,
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
