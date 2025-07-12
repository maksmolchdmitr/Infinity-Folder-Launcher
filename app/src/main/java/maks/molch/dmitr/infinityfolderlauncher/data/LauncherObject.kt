package maks.molch.dmitr.infinityfolderlauncher.data

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

sealed class LauncherObject(
    open val name: String,
)

data class Folder(
    override val name: String,
    val launcherObjects: List<LauncherObject> = mutableListOf(),
) : LauncherObject(name)

class Application(
    override val name: String,
    val packageName: String,
) : LauncherObject(name) {
    constructor(
        applicationInfo: ApplicationInfo,
        packageManager: PackageManager
    ) : this(
        name = applicationInfo.loadLabel(packageManager).toString(),
        packageName = applicationInfo.packageName,
    )

    fun getIcon(packageManager: PackageManager): Drawable =
        packageManager.getApplicationIcon(packageName)
}