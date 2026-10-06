package maks.molch.dmitr.infinityfolderlauncher.widget

import android.app.Activity
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.dao.SettingsDao
import maks.molch.dmitr.infinityfolderlauncher.data.AppWidgetItem
import java.util.UUID

/**
 * Owns [AppWidgetHost] lifecycle and pick/bind/configure flows for the launcher.
 */
class LauncherWidgetController(
    private val activity: ComponentActivity,
    private val folderDao: FolderDao,
    private val settingsDao: SettingsDao,
) {
    val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(activity)
    val host: AppWidgetHost = AppWidgetHost(activity, HOST_ID)

    private var pendingFolderId: String? = null
    private var pendingAppWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    private lateinit var pickLauncher: ActivityResultLauncher<Intent>
    private lateinit var bindLauncher: ActivityResultLauncher<Intent>
    private lateinit var configureLauncher: ActivityResultLauncher<Intent>

    fun register() {
        pickLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            val data = result.data
            val id = data?.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID,
            ) ?: AppWidgetManager.INVALID_APPWIDGET_ID
            if (result.resultCode != Activity.RESULT_OK || id == AppWidgetManager.INVALID_APPWIDGET_ID) {
                if (id != AppWidgetManager.INVALID_APPWIDGET_ID) host.deleteAppWidgetId(id)
                clearPending()
                return@registerForActivityResult
            }
            pendingAppWidgetId = id
            val info = appWidgetManager.getAppWidgetInfo(id)
            if (info == null) {
                host.deleteAppWidgetId(id)
                clearPending()
                return@registerForActivityResult
            }
            // PICK already binds on most devices; if not bound, request bind.
            if (appWidgetManager.getAppWidgetInfo(id) != null) {
                maybeConfigureOrSave(info, id)
            } else {
                requestBind(id, info.provider)
            }
        }

        bindLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            val id = pendingAppWidgetId
            if (result.resultCode != Activity.RESULT_OK ||
                id == AppWidgetManager.INVALID_APPWIDGET_ID
            ) {
                if (id != AppWidgetManager.INVALID_APPWIDGET_ID) host.deleteAppWidgetId(id)
                clearPending()
                return@registerForActivityResult
            }
            val info = appWidgetManager.getAppWidgetInfo(id)
            if (info == null) {
                host.deleteAppWidgetId(id)
                clearPending()
                return@registerForActivityResult
            }
            maybeConfigureOrSave(info, id)
        }

        configureLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            val id = pendingAppWidgetId
            if (result.resultCode != Activity.RESULT_OK ||
                id == AppWidgetManager.INVALID_APPWIDGET_ID
            ) {
                if (id != AppWidgetManager.INVALID_APPWIDGET_ID) host.deleteAppWidgetId(id)
                clearPending()
                return@registerForActivityResult
            }
            val info = appWidgetManager.getAppWidgetInfo(id) ?: run {
                host.deleteAppWidgetId(id)
                clearPending()
                return@registerForActivityResult
            }
            saveWidget(info, id)
        }
    }

    fun startListening() {
        host.startListening()
    }

    fun stopListening() {
        host.stopListening()
    }

    fun startPick(folderId: String) {
        pendingFolderId = folderId
        val appWidgetId = host.allocateAppWidgetId()
        pendingAppWidgetId = appWidgetId
        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        pickLauncher.launch(intent)
    }

    fun deleteWidgetIds(items: Collection<AppWidgetItem>) {
        for (item in items) {
            runCatching { host.deleteAppWidgetId(item.appWidgetId) }
        }
    }

    fun createView(context: Context, appWidgetId: Int): AppWidgetHostView {
        val info = appWidgetManager.getAppWidgetInfo(appWidgetId)
        return host.createView(context, appWidgetId, info)
    }

    private fun requestBind(appWidgetId: Int, provider: ComponentName) {
        val allowed = appWidgetManager.bindAppWidgetIdIfAllowed(appWidgetId, provider)
        if (allowed) {
            val info = appWidgetManager.getAppWidgetInfo(appWidgetId) ?: run {
                host.deleteAppWidgetId(appWidgetId)
                clearPending()
                return
            }
            maybeConfigureOrSave(info, appWidgetId)
            return
        }
        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, provider)
        }
        bindLauncher.launch(intent)
    }

    private fun maybeConfigureOrSave(info: AppWidgetProviderInfo, appWidgetId: Int) {
        val configure = info.configure
        if (configure != null) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE).apply {
                component = configure
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            runCatching { configureLauncher.launch(intent) }
                .onFailure {
                    // Some widgets declare configure but cannot be started — save as-is.
                    saveWidget(info, appWidgetId)
                }
        } else {
            saveWidget(info, appWidgetId)
        }
    }

    private fun saveWidget(info: AppWidgetProviderInfo, appWidgetId: Int) {
        val folderId = pendingFolderId ?: run {
            host.deleteAppWidgetId(appWidgetId)
            clearPending()
            return
        }
        val columns = settingsDao.mainColumns.value.coerceAtLeast(1)
        val (spanCols, spanRows) = AppWidgetSpans.fromProvider(activity, info, columns)
        val item = AppWidgetItem(
            id = UUID.randomUUID().toString(),
            name = AppWidgetSpans.labelOf(activity, info),
            appWidgetId = appWidgetId,
            provider = AppWidgetSpans.providerKey(info.provider),
            spanCols = spanCols,
            spanRows = spanRows,
        )
        folderDao.addObjectsAndSave(folderId, setOf(item))
        clearPending()
    }

    private fun clearPending() {
        pendingFolderId = null
        pendingAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    }

    companion object {
        const val HOST_ID = 0x49464C57 // "IFLW"
    }
}

fun AppWidgetHostView.updateSize(widthPx: Int, heightPx: Int) {
    val opts = Bundle().apply {
        putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, pxToDp(widthPx))
        putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, pxToDp(widthPx))
        putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, pxToDp(heightPx))
        putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, pxToDp(heightPx))
    }
    updateAppWidgetOptions(opts)
}

private fun AppWidgetHostView.pxToDp(px: Int): Int =
    (px / resources.displayMetrics.density).toInt().coerceAtLeast(1)
