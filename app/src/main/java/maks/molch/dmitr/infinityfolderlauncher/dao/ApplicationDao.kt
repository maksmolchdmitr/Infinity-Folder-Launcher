package maks.molch.dmitr.infinityfolderlauncher.dao

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class ApplicationDao(private val context: Context) {
    @SuppressLint("QueryPermissionsNeeded")
    fun getInstalledApplications(): List<Application> {
        val packageManager: PackageManager = context.packageManager
        val installedApplications: List<ApplicationInfo> = packageManager
            .getInstalledApplications(0)
        val applicationList: List<Application> = installedApplications
            .sortedBy { appInfo ->
                if (appInfo.isSystem()) 1 else 0
            }
            .map { appInfo ->
                Application(
                    name = appInfo.loadLabel(packageManager).toString(),
                    icon = appInfo.loadIcon(packageManager),
                )
            }
        return applicationList
    }

    private fun ApplicationInfo.isSystem(): Boolean =
        (this.flags and ApplicationInfo.FLAG_SYSTEM) == 1
}

data class Application(
    val name: String,
    val icon: Drawable,
)