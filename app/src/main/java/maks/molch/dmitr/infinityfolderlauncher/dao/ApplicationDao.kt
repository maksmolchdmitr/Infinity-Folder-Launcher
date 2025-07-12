package maks.molch.dmitr.infinityfolderlauncher.dao

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class ApplicationDao(private val context: Context) {
    @SuppressLint("QueryPermissionsNeeded", "NewApi")
    fun getInstalledApplications(): List<Application> {
        val packageManager: PackageManager = context.packageManager
        val installedApplications: List<ApplicationInfo> = packageManager
            .getInstalledApplications(PackageManager.GET_META_DATA)
        val applicationList: List<Application> = installedApplications
            .sortedBy { appInfo -> -appInfo.category }
            .map { appInfo -> Application(appInfo, packageManager) }
        return applicationList
    }
}

data class Application(
    val name: String,
    val icon: Drawable,
    val packageName: String,
    val category: Int,
) {
    @SuppressLint("NewApi")
    constructor(
        applicationInfo: ApplicationInfo,
        packageManager: PackageManager
    ) : this(
        name = applicationInfo.loadLabel(packageManager).toString(),
        icon = applicationInfo.loadIcon(packageManager),
        packageName = applicationInfo.packageName,
        category = applicationInfo.category,
    )
}