package maks.molch.dmitr.infinityfolderlauncher.dao

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import maks.molch.dmitr.infinityfolderlauncher.data.Application

class ApplicationDao(context: Context) {
    private val appContext = context.applicationContext
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mutex = Mutex()

    private val _apps = MutableStateFlow<List<Application>>(emptyList())
    val apps: StateFlow<List<Application>> = _apps.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private var receiverRegistered = false

    private val packageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val packageName = intent.data?.schemeSpecificPart ?: return
            when (intent.action) {
                Intent.ACTION_PACKAGE_REMOVED -> {
                    if (intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) return
                    removePackage(packageName)
                }

                Intent.ACTION_PACKAGE_ADDED,
                Intent.ACTION_PACKAGE_CHANGED,
                Intent.ACTION_PACKAGE_REPLACED,
                -> {
                    if (
                        intent.action == Intent.ACTION_PACKAGE_ADDED &&
                        intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)
                    ) {
                        return
                    }
                    upsertPackage(packageName)
                }
            }
        }
    }

    fun start() {
        registerPackageReceiver()
        refresh()
    }

    fun refresh() {
        scope.launch {
            mutex.withLock {
                _loading.value = true
                try {
                    _apps.value = loadLaunchableApps()
                } finally {
                    _loading.value = false
                }
            }
        }
    }

    private fun upsertPackage(packageName: String) {
        scope.launch {
            mutex.withLock {
                val app = loadLaunchableApp(packageName) ?: run {
                    _apps.value = _apps.value.filter { it.packageName != packageName }
                    return@withLock
                }
                _apps.value = (_apps.value.filter { it.packageName != packageName } + app)
                    .sortedBy { it.name.lowercase() }
            }
        }
    }

    private fun removePackage(packageName: String) {
        scope.launch {
            mutex.withLock {
                _apps.value = _apps.value.filter { it.packageName != packageName }
            }
        }
    }

    private fun loadLaunchableApps(): List<Application> {
        val packageManager = appContext.packageManager
        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        return packageManager.queryIntentActivities(launcherIntent, PackageManager.MATCH_ALL)
            .mapNotNull { resolveInfo ->
                val packageName = resolveInfo.activityInfo?.packageName ?: return@mapNotNull null
                Application(
                    id = packageName,
                    name = resolveInfo.loadLabel(packageManager).toString(),
                    packageName = packageName,
                )
            }
            .distinctBy { it.packageName }
            .sortedBy { it.name.lowercase() }
    }

    private fun loadLaunchableApp(packageName: String): Application? {
        val packageManager = appContext.packageManager
        val launcherIntent = Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)
            .setPackage(packageName)
        val resolveInfo = packageManager.queryIntentActivities(
            launcherIntent,
            PackageManager.MATCH_ALL,
        ).firstOrNull() ?: return null
        return Application(
            id = packageName,
            name = resolveInfo.loadLabel(packageManager).toString(),
            packageName = packageName,
        )
    }

    private fun registerPackageReceiver() {
        if (receiverRegistered) return
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addDataScheme("package")
        }
        ContextCompat.registerReceiver(
            appContext,
            packageReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        receiverRegistered = true
    }
}
