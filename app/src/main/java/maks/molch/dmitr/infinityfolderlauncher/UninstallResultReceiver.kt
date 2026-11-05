package maks.molch.dmitr.infinityfolderlauncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.Build
import android.util.Log

class UninstallResultReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE)
        when (status) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                val confirm = pendingUserActionIntent(intent) ?: return
                confirm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                runCatching { context.startActivity(confirm) }
                    .onFailure { Log.e(TAG, "Failed to show uninstall confirmation", it) }
            }

            PackageInstaller.STATUS_SUCCESS ->
                Log.i(TAG, "Uninstall success ${intent.getStringExtra(PackageInstaller.EXTRA_PACKAGE_NAME)}")

            else ->
                Log.w(
                    TAG,
                    "Uninstall status=$status message=${intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE)}",
                )
        }
    }

    private fun pendingUserActionIntent(intent: Intent): Intent? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Intent.EXTRA_INTENT)
        }
    }

    companion object {
        private const val TAG = "IFL_Uninstall"
    }
}
