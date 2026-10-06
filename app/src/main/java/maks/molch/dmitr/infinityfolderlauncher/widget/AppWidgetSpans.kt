package maks.molch.dmitr.infinityfolderlauncher.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

object AppWidgetSpans {
    private const val MAX_ROWS = 4

    fun fromProvider(
        context: Context,
        info: AppWidgetProviderInfo,
        columns: Int,
    ): Pair<Int, Int> {
        val colsAvailable = columns.coerceAtLeast(1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val tw = info.targetCellWidth.takeIf { it > 0 }
            val th = info.targetCellHeight.takeIf { it > 0 }
            if (tw != null && th != null) {
                return tw.coerceIn(1, colsAvailable) to th.coerceIn(1, MAX_ROWS)
            }
        }
        val metrics = context.resources.displayMetrics
        val cellW = cellWidthPx(metrics, colsAvailable)
        val cellH = cellW // approximate square cells on home grid
        val spanCols = ceil(info.minWidth.toDouble() / cellW.coerceAtLeast(1)).toInt()
            .coerceIn(1, colsAvailable)
        val spanRows = ceil(info.minHeight.toDouble() / cellH.coerceAtLeast(1)).toInt()
            .coerceIn(1, MAX_ROWS)
        return spanCols to max(1, min(spanRows, MAX_ROWS))
    }

    private fun cellWidthPx(metrics: DisplayMetrics, columns: Int): Int {
        // Match Main padding 16.dp * 2 + spacedBy 16.dp between columns.
        val density = metrics.density
        val horizontalPadding = 32f * density
        val gaps = 16f * density * (columns - 1).coerceAtLeast(0)
        val usable = metrics.widthPixels - horizontalPadding - gaps
        return (usable / columns).toInt().coerceAtLeast(1)
    }

    fun labelOf(context: Context, info: AppWidgetProviderInfo): String =
        info.loadLabel(context.packageManager).toString().ifBlank {
            info.provider.shortClassName
                .substringAfterLast('.')
                .ifBlank { info.provider.packageName }
        }

    fun providerKey(component: ComponentName): String = component.flattenToString()
}
