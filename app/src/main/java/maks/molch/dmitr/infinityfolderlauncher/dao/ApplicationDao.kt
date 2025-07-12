package maks.molch.dmitr.infinityfolderlauncher.dao

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import maks.molch.dmitr.infinityfolderlauncher.data.Application

class ApplicationDao(private val context: Context) {
    @SuppressLint("NewApi")
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