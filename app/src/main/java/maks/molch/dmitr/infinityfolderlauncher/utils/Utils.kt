package maks.molch.dmitr.infinityfolderlauncher.utils

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import maks.molch.dmitr.infinityfolderlauncher.R

const val MAIN_FOLDER_ID = "main"
const val MAIN_FOLDER_NAME = "MAIN_FOLDER"

const val ACTION_UNINSTALL_RESULT =
    "maks.molch.dmitr.infinityfolderlauncher.ACTION_UNINSTALL_RESULT"

private const val TAG = "IFL_Launch"

/**
 * Launch an app into its own task (Recents can switch back to it).
 * Matches AOSP Launcher3 flags: NEW_TASK | RESET_TASK_IF_NEEDED.
 */
fun launchApp(
    context: Context,
    packageName: String,
    activityName: String? = null,
): Boolean {
    val pm = context.packageManager
    val launch = if (!activityName.isNullOrBlank()) {
        Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            component = ComponentName(packageName, activityName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        }
    } else {
        buildLaunchIntent(pm, packageName)
    }
    if (launch == null) {
        Log.e(TAG, "No launch intent for $packageName activity=$activityName")
        Toast.makeText(context, "Не удалось открыть: $packageName", Toast.LENGTH_SHORT).show()
        return false
    }
    return try {
        Log.i(
            TAG,
            "startActivity pkg=$packageName activity=$activityName " +
                "component=${launch.component} flags=0x${Integer.toHexString(launch.flags)}",
        )
        context.startActivity(launch)
        true
    } catch (t: Throwable) {
        Log.e(TAG, "startActivity failed for $packageName", t)
        Toast.makeText(context, "Ошибка запуска: ${t.message}", Toast.LENGTH_SHORT).show()
        false
    }
}

private fun buildLaunchIntent(pm: PackageManager, packageName: String): Intent? {
    val fromPm = pm.getLaunchIntentForPackage(packageName)
    if (fromPm != null) {
        fromPm.action = Intent.ACTION_MAIN
        fromPm.addCategory(Intent.CATEGORY_LAUNCHER)
        fromPm.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        return fromPm
    }
    val query = Intent(Intent.ACTION_MAIN)
        .addCategory(Intent.CATEGORY_LAUNCHER)
        .setPackage(packageName)
    val resolve = pm.queryIntentActivities(query, 0).firstOrNull() ?: return null
    val activity = resolve.activityInfo ?: return null
    return Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
        component = ComponentName(activity.packageName, activity.name)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
    }
}

fun uninstallApp(context: Context, packageName: String): Boolean {
    if (packageName.isBlank()) return false
    if (uninstallViaPackageInstaller(context, packageName)) return true
    return uninstallViaDeleteIntent(context, packageName)
}

private fun uninstallViaPackageInstaller(context: Context, packageName: String): Boolean {
    return try {
        val appContext = context.applicationContext
        val intent = Intent(ACTION_UNINSTALL_RESULT).apply {
            setPackage(appContext.packageName)
            putExtra(PackageInstaller.EXTRA_PACKAGE_NAME, packageName)
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_MUTABLE
            } else {
                0
            }
        val sender = PendingIntent.getBroadcast(
            appContext,
            packageName.hashCode(),
            intent,
            flags,
        ).intentSender
        appContext.packageManager.packageInstaller.uninstall(packageName, sender)
        true
    } catch (t: Throwable) {
        Log.e(TAG, "PackageInstaller.uninstall failed for $packageName", t)
        false
    }
}

private fun uninstallViaDeleteIntent(context: Context, packageName: String): Boolean {
    val intent = Intent(Intent.ACTION_DELETE).apply {
        data = Uri.parse("package:$packageName")
        putExtra(Intent.EXTRA_RETURN_RESULT, true)
    }
    return try {
        context.startActivity(intent)
        true
    } catch (t: Throwable) {
        Log.e(TAG, "ACTION_DELETE failed for $packageName", t)
        Toast.makeText(
            context,
            context.getString(R.string.uninstall_failed),
            Toast.LENGTH_SHORT,
        ).show()
        false
    }
}
