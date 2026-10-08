package maks.molch.dmitr.infinityfolderlauncher.recents

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.graphics.Point
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class SystemRecentsRecoveryService : AccessibilityService() {
    private val handler = Handler(Looper.getMainLooper())
    private var recovery: Recovery? = null

    private class Recovery(
        val card: AccessibilityNodeInfo,
        val description: String,
        val bounds: Rect,
        val homePackage: String,
        var restoring: Boolean = false,
    )

    override fun onServiceConnected() {
        observeHomePackage()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            cancelRecovery()
            return
        }

        if (recovery?.restoring == true) return

        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            cancelRecovery()
            return
        }

        if (event.packageName?.toString() != MIUI_HOME) return

        if (event.eventType != AccessibilityEvent.TYPE_VIEW_CLICKED) return

        cancelRecovery()
        val source = event.source ?: return
        if (!isCard(source)) return

        val description = source.contentDescription?.toString() ?: return
        val root = recentsRoot() ?: return
        if (matchingCards(root, description).none { it == source }) return

        val home = observeHomePackage() ?: return
        val pending = Recovery(source, description, Rect().also(source::getBoundsInScreen), home)
        recovery = pending
        handler.postDelayed({ recoverIfStuck(pending) }, 1200)
    }

    override fun onInterrupt() {
        cancelRecovery()
    }

    override fun onDestroy() {
        cancelRecovery()
        super.onDestroy()
    }

    private fun observeHomePackage(): String? {
        val home = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
            .resolveActivity(packageManager)?.packageName
        val packages = listOfNotNull(MIUI_HOME, home).distinct().toTypedArray()
        if (!serviceInfo.packageNames.contentEquals(packages)) {
            serviceInfo = serviceInfo.apply { packageNames = packages }
        }
        return home
    }

    private fun recoverIfStuck(pending: Recovery) {
        if (recovery !== pending) return

        val root = recentsRoot()
        if (root == null || !pending.card.refresh() ||
            pending.card.contentDescription?.toString() != pending.description ||
            Rect().also(pending.card::getBoundsInScreen) != pending.bounds ||
            matchingCards(root, pending.description).none { it == pending.card }
        ) {
            cancelRecovery()
            return
        }

        val point = backgroundPoint(root)
        if (point == null) {
            cancelRecovery()
            return
        }

        pending.restoring = true
        val path = Path().apply { moveTo(point.x.toFloat(), point.y.toFloat()) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 40))
            .build()
        Log.i(TAG, "Recovering a stalled system Recents card")
        val dispatched = dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                awaitHome(pending, SystemClock.uptimeMillis() + 1500)
            }

            override fun onCancelled(gestureDescription: GestureDescription) {
                cancelRecovery()
            }
        }, handler)
        if (!dispatched) cancelRecovery()
    }

    private fun awaitHome(pending: Recovery, deadline: Long) {
        if (recovery !== pending) return

        val root = rootInActiveWindow
        if (root?.packageName?.toString() == pending.homePackage) {
            if (performGlobalAction(GLOBAL_ACTION_RECENTS)) {
                awaitRecents(pending, SystemClock.uptimeMillis() + 2000)
            } else {
                cancelRecovery()
            }
            return
        }

        if (SystemClock.uptimeMillis() >= deadline ||
            (root != null && root.packageName?.toString() != MIUI_HOME)
        ) {
            cancelRecovery()
            return
        }

        handler.postDelayed({ awaitHome(pending, deadline) }, 50)
    }

    private fun awaitRecents(pending: Recovery, deadline: Long) {
        if (recovery !== pending) return

        val root = recentsRoot()
        if (root != null) {
            val card = matchingCards(root, pending.description).singleOrNull { it == pending.card }
            if (card != null && card.isVisibleToUser && card.isEnabled) {
                handler.postDelayed({ retryCard(pending) }, 250)
                return
            }
        }

        if (SystemClock.uptimeMillis() >= deadline) {
            cancelRecovery()
            return
        }

        handler.postDelayed({ awaitRecents(pending, deadline) }, 50)
    }

    private fun retryCard(pending: Recovery) {
        if (recovery !== pending) return

        val root = recentsRoot()
        val card = root?.let { matchingCards(it, pending.description).singleOrNull { card -> card == pending.card } }
        if (card == null || !card.isVisibleToUser || !card.isEnabled) {
            cancelRecovery()
            return
        }

        val clicked = card.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        Log.i(TAG, "System card retry dispatched=$clicked")
        handler.postDelayed({ cancelRecovery() }, 1000)
    }

    private fun recentsRoot(): AccessibilityNodeInfo? {
        val root = rootInActiveWindow ?: return null
        if (root.packageName?.toString() != MIUI_HOME) return null

        return root.takeIf {
            it.findAccessibilityNodeInfosByViewId("$MIUI_HOME:id/recents_view").isNotEmpty()
        }
    }

    private fun isCard(node: AccessibilityNodeInfo): Boolean =
        node.isClickable && node.isVisibleToUser &&
            node.findAccessibilityNodeInfosByViewId("$MIUI_HOME:id/task_view_thumbnail").isNotEmpty()

    private fun matchingCards(root: AccessibilityNodeInfo, description: String): List<AccessibilityNodeInfo> =
        root.findAccessibilityNodeInfosByViewId("$MIUI_HOME:id/task_view_thumbnail")
            .mapNotNull { it.parent }
            .filter { isCard(it) && it.contentDescription?.toString() == description }
            .distinct()

    private fun backgroundPoint(root: AccessibilityNodeInfo): Point? {
        val bounds = Rect().also(root::getBoundsInScreen)
        val decorations = root.findAccessibilityNodeInfosByViewId("$MIUI_HOME:id/recents_decorations")
            .singleOrNull() ?: return null
        val contentBounds = Rect().also(decorations::getBoundsInScreen)
        val margin = (32 * resources.displayMetrics.density).toInt().coerceAtLeast(1)
        val safeTop = maxOf(bounds.top, contentBounds.top) + margin
        val safeBottom = minOf(bounds.bottom, contentBounds.bottom) - margin
        if (safeTop >= safeBottom) return null

        return sequenceOf(safeTop, safeTop + (safeBottom - safeTop) / 4, (safeTop + safeBottom) / 2)
            .map { Point(bounds.centerX(), it) }
            .firstOrNull { !hasClickableNodeAt(root, it) }
    }

    private fun hasClickableNodeAt(node: AccessibilityNodeInfo, point: Point): Boolean {
        val bounds = Rect().also(node::getBoundsInScreen)
        if (node.isClickable && node.isVisibleToUser && bounds.contains(point.x, point.y)) return true

        return (0 until node.childCount).any { index ->
            node.getChild(index)?.let { hasClickableNodeAt(it, point) } == true
        }
    }

    private fun cancelRecovery() {
        handler.removeCallbacksAndMessages(null)
        recovery = null
    }

    private companion object {
        const val MIUI_HOME = "com.miui.home"
        const val TAG = "IFL_Recents"
    }
}
