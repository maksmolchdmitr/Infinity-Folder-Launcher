package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import maks.molch.dmitr.infinityfolderlauncher.InfinityFolderApp
import maks.molch.dmitr.infinityfolderlauncher.LocalWidgetController
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.dao.SettingsDao
import maks.molch.dmitr.infinityfolderlauncher.data.AppWidgetItem
import maks.molch.dmitr.infinityfolderlauncher.data.Application
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.data.StepsWidget
import maks.molch.dmitr.infinityfolderlauncher.data.WebsiteShortcut
import maks.molch.dmitr.infinityfolderlauncher.ui.component.AppWidgetCell
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCell
import maks.molch.dmitr.infinityfolderlauncher.ui.component.ObjectCellState
import maks.molch.dmitr.infinityfolderlauncher.ui.component.SelectFolder
import maks.molch.dmitr.infinityfolderlauncher.ui.component.StepsHistoryDialog
import maks.molch.dmitr.infinityfolderlauncher.ui.component.StepsWidgetCell
import maks.molch.dmitr.infinityfolderlauncher.ui.component.calcState
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.AppRemoveDialog
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.ConfirmRemove
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.NavBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.Page
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextH4
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBarIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Done
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Edit
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Info
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Left
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Move
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Settings
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.FolderBackgrounds
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange20
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Red70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.ResolvedFolderBackground
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.resolveFolderBackground
import maks.molch.dmitr.infinityfolderlauncher.utils.MAIN_FOLDER_ID
import maks.molch.dmitr.infinityfolderlauncher.utils.launchApp
import kotlin.math.roundToInt

@Composable
fun MainScreen(
    context: Context,
    screen: MutableState<Screen>,
    folderStack: SnapshotStateList<String>,
    currentFolderId: String,
    folderDao: FolderDao,
    settingsDao: SettingsDao,
    homeReset: Int = 0,
    onEditFolder: (Folder) -> Unit,
) {
    val columns by settingsDao.mainColumns.collectAsState()
    val defaultFolderBackground by settingsDao.defaultFolderBackground.collectAsState()
    val defaultFolderBackgroundImages by settingsDao.defaultFolderBackgroundImages.collectAsState()
    val defaultFolderBackgroundRotateSeconds by
        settingsDao.defaultFolderBackgroundRotateSeconds.collectAsState()
    val rotateBackgroundOnFolderChange by
        settingsDao.rotateBackgroundOnFolderChange.collectAsState()
    val epoch by folderDao.epoch.collectAsState()
    val currentFolder = remember(epoch, currentFolderId) {
        folderDao.getOrCreate(currentFolderId)
    }
    val app = context.applicationContext as InfinityFolderApp
    val resolvedBackground = remember(
        currentFolderId,
        currentFolder.backgroundName,
        currentFolder.backgroundImages,
        currentFolder.backgroundRotateSeconds,
        defaultFolderBackground,
        defaultFolderBackgroundImages,
        defaultFolderBackgroundRotateSeconds,
    ) {
        resolveFolderBackground(
            folderPreset = currentFolder.backgroundName,
            folderImages = currentFolder.backgroundImages,
            folderRotateSeconds = currentFolder.backgroundRotateSeconds,
            defaultPreset = defaultFolderBackground,
            defaultImages = defaultFolderBackgroundImages,
            defaultRotateSeconds = defaultFolderBackgroundRotateSeconds,
            imageAbsolutePath = app.folderBackgroundStore::absolutePath,
        )
    }
    val slideshow = resolvedBackground as? ResolvedFolderBackground.Slideshow
    var slideshowIndex by remember { mutableIntStateOf(0) }
    var previousFolderIdForBg by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(slideshow?.absolutePaths) {
        val size = slideshow?.absolutePaths?.size ?: 0
        if (size > 0) {
            slideshowIndex %= size
        } else {
            slideshowIndex = 0
        }
    }
    LaunchedEffect(currentFolderId, rotateBackgroundOnFolderChange, slideshow?.absolutePaths) {
        val paths = slideshow?.absolutePaths.orEmpty()
        val prev = previousFolderIdForBg
        if (
            rotateBackgroundOnFolderChange &&
            paths.size >= 2 &&
            prev != null &&
            prev != currentFolderId
        ) {
            slideshowIndex = (slideshowIndex + 1) % paths.size
        }
        previousFolderIdForBg = currentFolderId
    }
    LaunchedEffect(slideshow?.absolutePaths, slideshow?.rotateSeconds) {
        val paths = slideshow?.absolutePaths.orEmpty()
        val seconds = slideshow?.rotateSeconds ?: return@LaunchedEffect
        if (paths.size < 2) return@LaunchedEffect
        while (true) {
            delay(seconds * 1000L)
            slideshowIndex = (slideshowIndex + 1) % paths.size
        }
    }
    val backgroundBitmap = remember(slideshow, slideshowIndex) {
        val paths = slideshow?.absolutePaths.orEmpty()
        val path = paths.getOrNull(slideshowIndex.coerceIn(0, (paths.size - 1).coerceAtLeast(0)))
        path?.let { runCatching { BitmapFactory.decodeFile(it)?.asImageBitmap() }.getOrNull() }
    }
    val view = LocalView.current
    val gridState = rememberLazyGridState()

    val editModeEnabled = remember { mutableStateOf(false) }
    val moveObjectsEnabled = remember { mutableStateOf(false) }
    val clearObjectsEnabled = remember { mutableStateOf(false) }
    val appActionEnabled = remember { mutableStateOf(false) }
    val widgetActionEnabled = remember { mutableStateOf(false) }
    val exitSelectionEnabled = remember { mutableStateOf(false) }
    val focusedObject = remember { mutableStateOf<LauncherObject?>(null) }
    val focusedBounds = remember { mutableStateOf<Rect?>(null) }
    val selectedObjects: MutableState<Set<LauncherObject>> = remember { mutableStateOf(setOf()) }

    var draggingId by remember { mutableStateOf<String?>(null) }
    var dragFromIndex by remember { mutableIntStateOf(-1) }
    var dragToIndex by remember { mutableIntStateOf(-1) }
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }

    var rootLayout by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val cellLayouts = remember { mutableMapOf<String, LayoutCoordinates>() }
    val density = LocalDensity.current

    val selectedSingleFolder = selectedObjects.value.singleOrNull() as? Folder
    val selectedSingleApp = selectedObjects.value.singleOrNull() as? Application
    val selectedSingleWidget = selectedObjects.value.singleOrNull() as? WebsiteShortcut
    val selectedSingleAppWidget = selectedObjects.value.singleOrNull() as? AppWidgetItem
    val selectedSingleSteps = selectedObjects.value.singleOrNull() as? StepsWidget
    val stepsHistoryWidget = remember { mutableStateOf<StepsWidget?>(null) }
    val launcherObjects = currentFolder.launcherObjects
    val widgetController = LocalWidgetController.current
    val configuration = LocalConfiguration.current
    val cellWidthDp = remember(columns, configuration.screenWidthDp) {
        val cols = columns.coerceAtLeast(1)
        val usable = configuration.screenWidthDp - 32 - 16 * (cols - 1).coerceAtLeast(0)
        (usable.toFloat() / cols).dp
    }

    LaunchedEffect(launcherObjects) {
        if (launcherObjects.any { it is StepsWidget }) {
            app.stepsDao.start()
        }
    }
    val title = if (currentFolderId == MAIN_FOLDER_ID) {
        stringResource(R.string.app_name)
    } else {
        currentFolder.name
    }
    val hasSelection = selectedObjects.value.isNotEmpty()
    val focus = focusedObject.value

    fun resetDrag() {
        draggingId = null
        dragFromIndex = -1
        dragToIndex = -1
        dragOffsetX = 0f
        dragOffsetY = 0f
    }

    fun clearFocus() {
        focusedObject.value = null
        focusedBounds.value = null
    }

    fun focusObject(obj: LauncherObject) {
        val root = rootLayout
        val cell = cellLayouts[obj.id]
        focusedBounds.value = if (
            root != null &&
            cell != null &&
            root.isAttached &&
            cell.isAttached
        ) {
            val topLeft = root.localPositionOf(cell, Offset.Zero)
            Rect(
                left = topLeft.x,
                top = topLeft.y,
                right = topLeft.x + cell.size.width,
                bottom = topLeft.y + cell.size.height,
            )
        } else {
            null
        }
        focusedObject.value = obj
    }

    fun exitEditMode() {
        editModeEnabled.value = false
        selectedObjects.value = setOf()
        appActionEnabled.value = false
        widgetActionEnabled.value = false
        exitSelectionEnabled.value = false
        resetDrag()
    }

    LaunchedEffect(homeReset) {
        if (homeReset == 0) return@LaunchedEffect
        clearFocus()
        exitSelectionEnabled.value = false
        moveObjectsEnabled.value = false
        clearObjectsEnabled.value = false
        appActionEnabled.value = false
        widgetActionEnabled.value = false
        exitEditMode()
    }

    fun requestExitEdit() {
        if (hasSelection) {
            exitSelectionEnabled.value = true
        } else {
            exitEditMode()
        }
    }

    fun finishDrag() {
        if (dragFromIndex < 0 || dragToIndex < 0 || dragFromIndex !in launcherObjects.indices) {
            resetDrag()
            return
        }
        // dragToIndex = desired index in the list AFTER removing the dragged item.
        val withoutSize = (launcherObjects.size - 1).coerceAtLeast(0)
        val finalIndex = dragToIndex.coerceIn(0, withoutSize)
        val preview = launcherObjects.toMutableList()
        val item = preview.removeAt(dragFromIndex)
        preview.add(finalIndex, item)
        if (preview.map { it.id } != launcherObjects.map { it.id }) {
            folderDao.moveObjectToIndex(currentFolderId, dragFromIndex, finalIndex)
        }
        resetDrag()
    }

    /**
     * Desired index in the list *after* the dragged item is removed.
     * Snaps to the row under the pointer and places the item on its own row
     * (wide widgets won't stick beside icons on the row above).
     */
    fun targetIndexForDrag(): Int {
        if (dragFromIndex < 0 || launcherObjects.isEmpty()) return 0
        val root = rootLayout ?: return dragFromIndex.coerceAtMost(0)
        val dragging = launcherObjects.getOrNull(dragFromIndex) ?: return 0
        val dragCoords = cellLayouts[dragging.id] ?: return 0
        if (!root.isAttached || !dragCoords.isAttached) return 0

        val cols = columns.coerceAtLeast(1)
        val rows = rowRanges(launcherObjects, cols)
        if (rows.isEmpty()) return 0

        val origin = root.localPositionOf(dragCoords, Offset.Zero)
        val probeY = origin.y + dragCoords.size.height * 0.7f + dragOffsetY

        data class RowGeom(val range: IntRange, val top: Float, val bottom: Float)

        val geoms = rows.mapNotNull { range ->
            var top = Float.POSITIVE_INFINITY
            var bottom = Float.NEGATIVE_INFINITY
            var any = false
            for (i in range) {
                val obj = launcherObjects.getOrNull(i) ?: continue
                val coords = cellLayouts[obj.id] ?: continue
                if (!coords.isAttached) continue
                val tl = root.localPositionOf(coords, Offset.Zero)
                top = minOf(top, tl.y)
                bottom = maxOf(bottom, tl.y + coords.size.height)
                any = true
            }
            if (!any) null else RowGeom(range, top, bottom)
        }
        if (geoms.isEmpty()) return 0

        val containing = geoms.firstOrNull { probeY >= it.top && probeY <= it.bottom }
        val targetGeom = containing ?: geoms.minBy { geom ->
            kotlin.math.abs(probeY - (geom.top + geom.bottom) / 2f)
        }

        val sourceRow = rows.firstOrNull { dragFromIndex in it }
        if (sourceRow != null && targetGeom.range == sourceRow) {
            // Same visual row — keep relative final index (= how many items before us stay before).
            return dragFromIndex
        }

        val widgetSpan = spanOf(dragging, cols)
        val without = launcherObjects.filterIndexed { index, _ -> index != dragFromIndex }

        // First item of the target row that isn't the dragged widget.
        val anchorId = targetGeom.range
            .mapNotNull { launcherObjects.getOrNull(it)?.id }
            .firstOrNull { it != dragging.id }

        val preferredInWithout = when {
            anchorId != null -> without.indexOfFirst { it.id == anchorId }.coerceAtLeast(0)
            // Dropped below everything — append.
            probeY > (geoms.lastOrNull()?.bottom ?: 0f) -> without.size
            else -> 0
        }

        return insertIndexForRowWidget(without, preferredInWithout, widgetSpan, cols)
    }

    fun requestDelete(obj: LauncherObject) {
        clearFocus()
        selectedObjects.value = setOf(obj)
        when (obj) {
            is Application -> appActionEnabled.value = true
            is WebsiteShortcut, is AppWidgetItem, is StepsWidget -> widgetActionEnabled.value = true
            is Folder -> clearObjectsEnabled.value = true
        }
    }

    fun openAppInfo(app: Application) {
        clearFocus()
        runCatching {
            context.startActivity(
                Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:${app.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                },
            )
        }
    }

    BackHandler(enabled = focus != null) {
        clearFocus()
    }
    BackHandler(enabled = exitSelectionEnabled.value) {
        exitSelectionEnabled.value = false
    }
    BackHandler(enabled = appActionEnabled.value || widgetActionEnabled.value) {
        appActionEnabled.value = false
        widgetActionEnabled.value = false
    }
    BackHandler(enabled = editModeEnabled.value && moveObjectsEnabled.value) {
        moveObjectsEnabled.value = false
    }
    BackHandler(
        enabled = editModeEnabled.value &&
            !moveObjectsEnabled.value &&
            !appActionEnabled.value &&
            !widgetActionEnabled.value &&
            !exitSelectionEnabled.value &&
            draggingId == null &&
            focus == null,
    ) {
        requestExitEdit()
    }

    val overlayOpen =
        moveObjectsEnabled.value ||
            clearObjectsEnabled.value ||
            appActionEnabled.value ||
            widgetActionEnabled.value ||
            exitSelectionEnabled.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { rootLayout = it },
    ) {
        when {
            backgroundBitmap != null -> {
                Image(
                    bitmap = backgroundBitmap,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }

            else -> {
                val presetId = (resolvedBackground as? ResolvedFolderBackground.Preset)?.id
                    ?: FolderBackgrounds.DEFAULT_ID
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FolderBackgrounds.brushFor(presetId)),
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .then(if (focus != null) Modifier.blur(16.dp) else Modifier),
        ) {
            // Keep TopBar mounted while focused so the grid does not jump.
            if (!overlayOpen) {
                when {
                    editModeEnabled.value -> {
                        TopBar(
                            label = stringResource(R.string.edit_mode),
                            leftIcon = if (!hasSelection) {
                                TopBarIcon(Icons.Settings) {
                                    screen.value = Screen.Settings
                                }
                            } else {
                                null
                            },
                            firstRightIcon = if (selectedSingleFolder != null) {
                                TopBarIcon(Icons.Edit) {
                                    onEditFolder(selectedSingleFolder)
                                    exitEditMode()
                                }
                            } else {
                                null
                            },
                            secondRightIcon = TopBarIcon(
                                icon = Icons.Move,
                                enabled = hasSelection,
                            ) { moveObjectsEnabled.value = true },
                            thirdRightIcon = TopBarIcon(
                                icon = Icons.Done,
                                color = Green50,
                            ) { requestExitEdit() },
                        )
                    }

                    currentFolderId != MAIN_FOLDER_ID -> {
                        TopBar(
                            label = title,
                            leftIcon = TopBarIcon(Icons.Left) {
                                if (folderStack.size > 1) {
                                    folderStack.removeAt(folderStack.lastIndex)
                                }
                            },
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .emptyAreaLongPress(
                        enabled = !overlayOpen && focus == null,
                    ) {
                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        if (editModeEnabled.value) {
                            if (selectedObjects.value.isEmpty()) exitEditMode()
                        } else {
                            editModeEnabled.value = true
                        }
                    },
            ) {
                val gridColumns = columns.coerceAtLeast(1)
                val lastRowEmptySlots = remember(launcherObjects, gridColumns) {
                    emptySlotsInLastRow(launcherObjects, gridColumns)
                }
                val contentScrolls = gridState.canScrollForward || gridState.canScrollBackward
                LazyVerticalGrid(
                    state = gridState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    columns = GridCells.Fixed(gridColumns),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    // A scrollable that cannot actually move still wins the gesture
                    // and drops short taps. Enable scrolling only when content overflows.
                    userScrollEnabled = contentScrolls && draggingId == null && focus == null,
                ) {
                    itemsIndexed(
                        items = launcherObjects,
                        key = { _, item -> item.id },
                        span = { _, obj ->
                            val spanCols = when (obj) {
                                is AppWidgetItem -> obj.spanCols.coerceIn(1, maxLineSpan)
                                is StepsWidget -> obj.spanCols.coerceIn(1, maxLineSpan)
                                else -> 1
                            }
                            GridItemSpan(spanCols)
                        },
                    ) { index, obj ->
                        Box(
                            modifier = Modifier.onGloballyPositioned { coords ->
                                cellLayouts[obj.id] = coords
                            },
                        ) {
                            when (obj) {
                                is AppWidgetItem -> {
                                    AppWidgetCell(
                                        item = obj,
                                        cellWidth = cellWidthDp,
                                        editModeEnabled = editModeEnabled,
                                        selectedObjects = selectedObjects,
                                        wobble = editModeEnabled.value && !overlayOpen,
                                        showDelete = editModeEnabled.value,
                                        isDragging = draggingId == obj.id,
                                        dragOffset = if (draggingId == obj.id) {
                                            Offset(dragOffsetX, dragOffsetY)
                                        } else {
                                            Offset.Zero
                                        },
                                        dragEnabled = editModeEnabled.value &&
                                            !overlayOpen &&
                                            focus == null,
                                        onDragStart = {
                                            draggingId = obj.id
                                            dragFromIndex = index
                                            dragToIndex = index
                                            dragOffsetX = 0f
                                            dragOffsetY = 0f
                                            selectedObjects.value = setOf(obj)
                                            view.performHapticFeedback(
                                                HapticFeedbackConstants.LONG_PRESS,
                                            )
                                        },
                                        onDrag = { amount ->
                                            dragOffsetX += amount.x
                                            dragOffsetY += amount.y
                                            dragToIndex = targetIndexForDrag()
                                        },
                                        onDragEnd = { finishDrag() },
                                        onDragCancel = { resetDrag() },
                                        onDelete = { requestDelete(obj) },
                                        onLongPress = {
                                            if (!editModeEnabled.value) {
                                                view.performHapticFeedback(
                                                    HapticFeedbackConstants.LONG_PRESS,
                                                )
                                                focusObject(obj)
                                            }
                                        },
                                    ) {
                                        if (draggingId != null || focus != null) return@AppWidgetCell
                                        if (editModeEnabled.value) {
                                            val state = calcState(selectedObjects, obj)
                                            if (state == ObjectCellState.SelectionBlank) {
                                                selectedObjects.value += obj
                                            } else {
                                                selectedObjects.value =
                                                    selectedObjects.value
                                                        .filter { it.id != obj.id }
                                                        .toSet()
                                            }
                                        }
                                    }
                                }

                                is StepsWidget -> {
                                    StepsWidgetCell(
                                        item = obj,
                                        cellWidth = cellWidthDp,
                                        editModeEnabled = editModeEnabled,
                                        selectedObjects = selectedObjects,
                                        wobble = editModeEnabled.value && !overlayOpen,
                                        showDelete = editModeEnabled.value,
                                        isDragging = draggingId == obj.id,
                                        dragOffset = if (draggingId == obj.id) {
                                            Offset(dragOffsetX, dragOffsetY)
                                        } else {
                                            Offset.Zero
                                        },
                                        dragEnabled = editModeEnabled.value &&
                                            !overlayOpen &&
                                            focus == null,
                                        onDragStart = {
                                            draggingId = obj.id
                                            dragFromIndex = index
                                            dragToIndex = index
                                            dragOffsetX = 0f
                                            dragOffsetY = 0f
                                            selectedObjects.value = setOf(obj)
                                            view.performHapticFeedback(
                                                HapticFeedbackConstants.LONG_PRESS,
                                            )
                                        },
                                        onDrag = { amount ->
                                            dragOffsetX += amount.x
                                            dragOffsetY += amount.y
                                            dragToIndex = targetIndexForDrag()
                                        },
                                        onDragEnd = { finishDrag() },
                                        onDragCancel = { resetDrag() },
                                        onDelete = { requestDelete(obj) },
                                        onLongPress = {
                                            if (!editModeEnabled.value) {
                                                view.performHapticFeedback(
                                                    HapticFeedbackConstants.LONG_PRESS,
                                                )
                                                focusObject(obj)
                                            }
                                        },
                                    ) {
                                        if (draggingId != null || focus != null) return@StepsWidgetCell
                                        if (editModeEnabled.value) {
                                            val state = calcState(selectedObjects, obj)
                                            if (state == ObjectCellState.SelectionBlank) {
                                                selectedObjects.value += obj
                                            } else {
                                                selectedObjects.value =
                                                    selectedObjects.value
                                                        .filter { it.id != obj.id }
                                                        .toSet()
                                            }
                                        } else {
                                            stepsHistoryWidget.value = obj
                                        }
                                    }
                                }

                                else -> ObjectCell(
                                    context = context,
                                    launcherObject = obj,
                                    editModeEnabled = editModeEnabled,
                                    selectedObjects = selectedObjects,
                                    folderDao = folderDao,
                                    isDragging = draggingId == obj.id,
                                    dragOffset = if (draggingId == obj.id) {
                                        Offset(dragOffsetX, dragOffsetY)
                                    } else {
                                        Offset.Zero
                                    },
                                    dragEnabled = editModeEnabled.value &&
                                        !overlayOpen &&
                                        focus == null,
                                    wobble = editModeEnabled.value && !overlayOpen,
                                    onDragStart = {
                                        draggingId = obj.id
                                        dragFromIndex = index
                                        dragToIndex = index
                                        dragOffsetX = 0f
                                        dragOffsetY = 0f
                                        selectedObjects.value = setOf(obj)
                                        view.performHapticFeedback(
                                            HapticFeedbackConstants.LONG_PRESS,
                                        )
                                    },
                                    onDrag = { amount ->
                                        dragOffsetX += amount.x
                                        dragOffsetY += amount.y
                                        dragToIndex = targetIndexForDrag()
                                    },
                                    onDragEnd = { finishDrag() },
                                    onDragCancel = { resetDrag() },
                                    onDelete = { requestDelete(obj) },
                                    onLongPress = {
                                        if (!editModeEnabled.value) {
                                            view.performHapticFeedback(
                                                HapticFeedbackConstants.LONG_PRESS,
                                            )
                                            focusObject(obj)
                                        }
                                    },
                                ) {
                                    if (draggingId != null || focus != null) return@ObjectCell
                                    if (editModeEnabled.value) {
                                        val state = calcState(selectedObjects, obj)
                                        if (state == ObjectCellState.SelectionBlank) {
                                            selectedObjects.value += obj
                                        } else {
                                            selectedObjects.value =
                                                selectedObjects.value
                                                    .filter { it.id != obj.id }
                                                    .toSet()
                                        }
                                        return@ObjectCell
                                    }
                                    when (obj) {
                                        is Application -> {
                                            launchApp(context, obj.packageName, obj.activityName)
                                        }

                                        is Folder -> {
                                            if (folderStack.lastOrNull() != obj.id) {
                                                folderStack.add(obj.id)
                                            }
                                        }

                                        is WebsiteShortcut -> {
                                            runCatching {
                                                context.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(obj.url),
                                                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                                                )
                                            }
                                        }

                                        is AppWidgetItem, is StepsWidget -> Unit
                                    }
                                }
                            }
                        }
                    }

                    // Empty cells on the right of the last row. The area below the
                    // grid is handled by emptyAreaLongPress, without a tall spacer:
                    // that spacer made the grid always scroll and ate icon taps.
                    items(
                        count = lastRowEmptySlots,
                        key = { "last_row_pad_$it" },
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(96.dp)
                                .pointerInput(editModeEnabled.value, overlayOpen, focus) {
                                    if (overlayOpen || focus != null) return@pointerInput
                                    detectTapGestures(
                                        onLongPress = {
                                            view.performHapticFeedback(
                                                HapticFeedbackConstants.LONG_PRESS,
                                            )
                                            if (editModeEnabled.value) {
                                                if (!hasSelection) exitEditMode()
                                            } else {
                                                editModeEnabled.value = true
                                            }
                                        },
                                    )
                                },
                        )
                    }
                }
            }

            if (!overlayOpen && focus == null && editModeEnabled.value && !hasSelection) {
                NavBar(Page.Home, screen)
            }
        }

        if (focus != null) {
            val bounds = focusedBounds.value
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x66000000))
                    .clickable { clearFocus() },
            ) {
                Box(
                    modifier = Modifier
                        .then(
                            if (bounds != null) {
                                Modifier
                                    .offset {
                                        IntOffset(bounds.left.roundToInt(), bounds.top.roundToInt())
                                    }
                                    .width(with(density) { bounds.width.toDp() })
                            } else {
                                Modifier.align(Alignment.Center)
                            },
                        )
                        .clickable { },
                ) {
                    FocusedObjectActions(
                        context = context,
                        obj = focus,
                        folderDao = folderDao,
                        cellWidth = cellWidthDp,
                        onDelete = { requestDelete(focus) },
                        onEditFolder = {
                            clearFocus()
                            onEditFolder(it)
                        },
                        onAppInfo = { openAppInfo(it) },
                    )
                }
            }
        }
    }

    if (moveObjectsEnabled.value) {
        Overlay {
            SelectFolder(
                folderDao = folderDao,
                currentFolderId = currentFolderId,
                selectedObjects = selectedObjects,
                moveObjectsEnabled = moveObjectsEnabled,
                editModeEnabled = editModeEnabled,
            )
        }
    }

    if (exitSelectionEnabled.value) {
        Overlay(onDismiss = { exitSelectionEnabled.value = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Orange20)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                TextH4(text = stringResource(R.string.exit_selection_title))
                Text(
                    text = stringResource(R.string.exit_selection_clear),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Base0)
                        .clickable {
                            exitSelectionEnabled.value = false
                            exitEditMode()
                        }
                        .padding(14.dp),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                )
                Text(
                    text = stringResource(R.string.exit_selection_move),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Green50)
                        .clickable {
                            exitSelectionEnabled.value = false
                            moveObjectsEnabled.value = true
                        }
                        .padding(14.dp),
                    color = Base0,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                )
            }
        }
    }

    if (appActionEnabled.value && selectedSingleApp != null) {
        val app = selectedSingleApp
        Overlay(onDismiss = { appActionEnabled.value = false }) {
            AppRemoveDialog(
                appName = app.name,
                onCancel = { appActionEnabled.value = false },
                onRemoveFromFolder = {
                    folderDao.removeObjectsAndSave(currentFolderId, setOf(app))
                    selectedObjects.value = setOf()
                    appActionEnabled.value = false
                },
                onUninstall = {
                    folderDao.removeObjectsAndSave(currentFolderId, setOf(app))
                    selectedObjects.value = setOf()
                    appActionEnabled.value = false
                    runCatching {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_DELETE,
                                Uri.parse("package:${app.packageName}"),
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        )
                    }
                },
            )
        }
    }

    stepsHistoryWidget.value?.let { stepsWidget ->
        Overlay(onDismiss = { stepsHistoryWidget.value = null }) {
            StepsHistoryDialog(
                dailyGoal = stepsWidget.dailyGoal,
                onDismiss = { stepsHistoryWidget.value = null },
            )
        }
    }

    if (widgetActionEnabled.value &&
        (selectedSingleWidget != null ||
            selectedSingleAppWidget != null ||
            selectedSingleSteps != null)
    ) {
        val website = selectedSingleWidget
        val appWidget = selectedSingleAppWidget
        val steps = selectedSingleSteps
        Overlay(onDismiss = { widgetActionEnabled.value = false }) {
            ConfirmRemove(
                mainText = stringResource(
                    R.string.remove_widget_title,
                    website?.name ?: appWidget?.name ?: steps?.name.orEmpty(),
                ),
                descriptionText = stringResource(
                    when {
                        appWidget != null -> R.string.remove_appwidget_desc
                        steps != null -> R.string.remove_appwidget_desc
                        else -> R.string.remove_widget_desc
                    },
                ),
                removeText = stringResource(R.string.clear),
                onCancelClick = { widgetActionEnabled.value = false },
                onRemoveClick = {
                    when {
                        appWidget != null -> {
                            widgetController?.deleteWidgetIds(listOf(appWidget))
                            folderDao.removeObjectsAndSave(currentFolderId, setOf(appWidget))
                        }
                        steps != null -> {
                            folderDao.removeObjectsAndSave(currentFolderId, setOf(steps))
                        }
                        website != null -> {
                            folderDao.removeObjectsAndSave(currentFolderId, setOf(website))
                        }
                    }
                    selectedObjects.value = setOf()
                    widgetActionEnabled.value = false
                },
            )
        }
    }

    if (clearObjectsEnabled.value) {
        val singleFolder = selectedObjects.value.singleOrNull() as? Folder
        val fullFolder = singleFolder?.let { folderDao.getById(it.id) ?: it }
        Overlay(onDismiss = { clearObjectsEnabled.value = false }) {
            ConfirmRemove(
                mainText = if (singleFolder != null) {
                    stringResource(R.string.remove_folder_title, singleFolder.name)
                } else {
                    stringResource(R.string.clear_selected, selectedObjects.value.size)
                },
                descriptionText = when {
                    fullFolder == null -> stringResource(R.string.clear_selected_desc)
                    fullFolder.deleteContentsOnRemove ->
                        stringResource(R.string.remove_folder_with_contents_desc)
                    else -> stringResource(R.string.remove_folder_desc)
                },
                removeText = stringResource(R.string.clear),
                onCancelClick = {
                    clearObjectsEnabled.value = false
                },
                onRemoveClick = {
                    val widgets = selectedObjects.value.filterIsInstance<AppWidgetItem>()
                    widgetController?.deleteWidgetIds(widgets)
                    folderDao.removeObjectsAndSave(currentFolderId, selectedObjects.value)
                    selectedObjects.value = setOf()
                    clearObjectsEnabled.value = false
                },
            )
        }
    }
}

@Composable
private fun FocusedObjectActions(
    context: Context,
    obj: LauncherObject,
    folderDao: FolderDao,
    cellWidth: androidx.compose.ui.unit.Dp,
    onDelete: () -> Unit,
    onEditFolder: (Folder) -> Unit,
    onAppInfo: (Application) -> Unit,
) {
    val dummyEdit = remember { mutableStateOf(false) }
    val dummySelected = remember { mutableStateOf<Set<LauncherObject>>(emptySet()) }
    Box(modifier = Modifier.fillMaxWidth()) {
        when (obj) {
            is AppWidgetItem -> {
                AppWidgetCell(
                    item = obj,
                    cellWidth = cellWidth,
                    editModeEnabled = dummyEdit,
                    selectedObjects = dummySelected,
                    showDelete = true,
                    onDelete = onDelete,
                )
            }

            is StepsWidget -> {
                StepsWidgetCell(
                    item = obj,
                    cellWidth = cellWidth,
                    editModeEnabled = dummyEdit,
                    selectedObjects = dummySelected,
                    showDelete = true,
                    onDelete = onDelete,
                )
            }

            else -> {
                ObjectCell(
                    context = context,
                    launcherObject = obj,
                    editModeEnabled = dummyEdit,
                    selectedObjects = dummySelected,
                    folderDao = folderDao,
                    showDelete = true,
                    fillMaxWidth = true,
                    onDelete = onDelete,
                )
                when (obj) {
                    is Application -> {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(start = 0.dp, top = 2.dp)
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Base0)
                                .clickable { onAppInfo(obj) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Info,
                                contentDescription = stringResource(R.string.app_info),
                                tint = Red70,
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }

                    is Folder -> {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 54.dp, end = 0.dp)
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Base0)
                                .clickable { onEditFolder(folderDao.getById(obj.id) ?: obj) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Edit,
                                contentDescription = null,
                                tint = Red70,
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }

                    is WebsiteShortcut, is AppWidgetItem, is StepsWidget -> Unit
                }
            }
        }
    }
}

@Composable
private fun Overlay(onDismiss: (() -> Unit)? = null, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .background(Color(0x66000000))
            .clickable { onDismiss?.invoke() }
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.clickable { }) {
            content()
        }
    }
}

private fun spanOf(obj: LauncherObject, columns: Int): Int {
    val cols = columns.coerceAtLeast(1)
    return when (obj) {
        is AppWidgetItem -> obj.spanCols.coerceIn(1, cols)
        is StepsWidget -> obj.spanCols.coerceIn(1, cols)
        else -> 1
    }
}

/**
 * Insert index in [without] (list already without the dragged item) so a [widgetSpan]-wide
 * item starts on its own row at/after [preferredIndex], instead of sticking to the row above.
 */
private fun insertIndexForRowWidget(
    without: List<LauncherObject>,
    preferredIndex: Int,
    widgetSpan: Int,
    columns: Int,
): Int {
    val cols = columns.coerceAtLeast(1)
    val preferred = preferredIndex.coerceIn(0, without.size)
    var used = 0
    for (i in 0 until preferred) {
        val span = spanOf(without[i], cols)
        if (used > 0 && used + span > cols) used = 0
        used += span
        if (used >= cols) used = 0
    }
    // Already a row boundary, or widget won't fit in the leftover → grid wraps to new row.
    if (used == 0 || used + widgetSpan > cols) return preferred

    // Would stick to the incomplete row above — pull items from the target row up
    // until that row is full, then place the widget (starts the next row).
    var need = cols - used
    var index = preferred
    while (index < without.size && need > 0) {
        need -= spanOf(without[index], cols)
        index++
    }
    return index.coerceIn(0, without.size)
}

/**
 * Long-press on empty grid space. Listens on the final pass and ignores
 * events an icon already consumed, so a tap or the system home gesture
 * is not swallowed.
 */
private fun Modifier.emptyAreaLongPress(
    enabled: Boolean,
    onLongPress: () -> Unit,
): Modifier {
    if (!enabled) return this
    return pointerInput(Unit) {
        val timeout = viewConfiguration.longPressTimeoutMillis
        val touchSlop = viewConfiguration.touchSlop
        awaitEachGesture {
            val down = awaitFirstDown(
                requireUnconsumed = false,
                pass = PointerEventPass.Initial,
            )
            val pointerId = down.id
            val origin = down.position
            var change = down
            while (true) {
                val event = withTimeoutOrNull(timeout) {
                    awaitPointerEvent(PointerEventPass.Final)
                }
                if (event == null) {
                    if (!change.isConsumed && change.pressed) {
                        onLongPress()
                        while (true) {
                            val next = awaitPointerEvent(PointerEventPass.Final)
                            val held = next.changes.firstOrNull { it.id == pointerId } ?: break
                            held.consume()
                            if (!held.pressed) break
                        }
                    }
                    break
                }
                val next = event.changes.firstOrNull { it.id == pointerId } ?: break
                change = next
                if (next.isConsumed || !next.pressed) break
                if ((next.position - origin).getDistance() > touchSlop) break
            }
        }
    }
}

/** Packed row ranges (inclusive), matching LazyVerticalGrid span wrapping. */
private fun rowRanges(objects: List<LauncherObject>, columns: Int): List<IntRange> {
    val cols = columns.coerceAtLeast(1)
    if (objects.isEmpty()) return emptyList()
    val rows = ArrayList<IntRange>()
    var rowStart = 0
    var used = 0
    objects.forEachIndexed { index, obj ->
        val span = spanOf(obj, cols)
        if (used > 0 && used + span > cols) {
            rows += rowStart until index
            rowStart = index
            used = 0
        }
        used += span
        if (used >= cols) {
            rows += rowStart..index
            rowStart = index + 1
            used = 0
        }
    }
    if (rowStart <= objects.lastIndex) {
        rows += rowStart..objects.lastIndex
    }
    return rows
}

/** How many unit cells are free at the end of the last packed row. */
private fun emptySlotsInLastRow(objects: List<LauncherObject>, columns: Int): Int {
    val cols = columns.coerceAtLeast(1)
    var usedInRow = 0
    for (obj in objects) {
        val span = spanOf(obj, cols)
        if (usedInRow + span > cols) {
            usedInRow = 0
        }
        usedInRow += span
        if (usedInRow >= cols) {
            usedInRow = 0
        }
    }
    return if (usedInRow == 0) 0 else cols - usedInRow
}
