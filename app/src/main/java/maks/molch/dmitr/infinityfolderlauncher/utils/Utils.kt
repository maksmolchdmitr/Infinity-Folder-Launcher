package maks.molch.dmitr.infinityfolderlauncher.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast

const val MAIN_FOLDER_ID = "main"
const val MAIN_FOLDER_NAME = "MAIN_FOLDER"

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
