package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.dao.SettingsDao
import maks.molch.dmitr.infinityfolderlauncher.ui.component.FolderBackgroundPicker
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextBodyS
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextH4
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBar
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TopBarIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.CheckboxBlank
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.CheckboxMarked
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Done
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base5
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.ContrastLabel
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.FolderBackgrounds
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.IconLabelStyle
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange20
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    screen: MutableState<Screen>,
    settingsDao: SettingsDao,
) {
    val columns by settingsDao.mainColumns.collectAsState()
    val defaultFolderBackground by settingsDao.defaultFolderBackground.collectAsState()
    val defaultFolderBackgroundImages by settingsDao.defaultFolderBackgroundImages.collectAsState()
    val defaultFolderBackgroundRotateSeconds by
        settingsDao.defaultFolderBackgroundRotateSeconds.collectAsState()
    val rotateBackgroundOnFolderChange by
        settingsDao.rotateBackgroundOnFolderChange.collectAsState()
    val labelFontSizeSp by settingsDao.labelFontSizeSp.collectAsState()
    val labelColorArgb by settingsDao.labelColorArgb.collectAsState()
    val labelColor = Color(labelColorArgb)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Base5)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        TopBar(
            label = stringResource(R.string.settings),
            secondRightIcon = TopBarIcon(
                icon = Icons.Done,
                color = Green50,
            ) {
                screen.value = Screen.Main
            },
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Orange20, RoundedCornerShape(28.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                TextH4(text = stringResource(R.string.settings_tile_count))
                Text(
                    text = "$columns/${SettingsDao.MAX_COLUMNS}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Green50,
                )
                DiscreteSlider(
                    value = columns,
                    min = SettingsDao.MIN_COLUMNS,
                    max = SettingsDao.MAX_COLUMNS,
                    onValueChange = { settingsDao.setMainColumns(it) },
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Base0, RoundedCornerShape(12.dp))
                            .clickable { settingsDao.setMainColumns(columns - 1) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "−",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Base40,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Green50, RoundedCornerShape(12.dp))
                            .clickable { settingsDao.setMainColumns(columns + 1) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "+",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Base0,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            FolderBackgroundPicker(
                selectedPresetId = if (defaultFolderBackgroundImages.isNotEmpty()) {
                    null
                } else {
                    defaultFolderBackground
                },
                selectedImageFiles = defaultFolderBackgroundImages,
                selectedRotateSeconds = defaultFolderBackgroundRotateSeconds,
                onSelectPreset = { id -> settingsDao.setDefaultFolderBackground(id) },
                onSelectImages = { files ->
                    settingsDao.setDefaultFolderBackgroundImages(files)
                },
                onSelectRotateSeconds = { seconds ->
                    settingsDao.setDefaultFolderBackgroundRotateSeconds(seconds)
                },
                includeUseDefault = false,
                title = stringResource(R.string.folder_background_default),
            )
            if (defaultFolderBackgroundImages.size >= 2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Orange20, RoundedCornerShape(28.dp))
                        .clickable {
                            settingsDao.setRotateBackgroundOnFolderChange(
                                !rotateBackgroundOnFolderChange,
                            )
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(
                        imageVector = if (rotateBackgroundOnFolderChange) {
                            Icons.CheckboxMarked
                        } else {
                            Icons.CheckboxBlank
                        },
                        contentDescription = null,
                        tint = if (rotateBackgroundOnFolderChange) Green50 else Base40,
                        modifier = Modifier.size(28.dp),
                    )
                    TextBodyS(
                        text = stringResource(R.string.folder_background_rotate_on_navigate),
                        color = Base70,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            LabelStyleSettings(
                fontSizeSp = labelFontSizeSp,
                color = labelColor,
                onFontSizeChange = settingsDao::setLabelFontSizeSp,
                onColorChange = settingsDao::setLabelColor,
            )
            FixSystemRecentsCard()
        }
    }
}

@Composable
private fun FixSystemRecentsCard() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Orange20, RoundedCornerShape(28.dp))
            .clickable {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", "com.miui.home", null)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                runCatching { context.startActivity(intent) }
            }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TextH4(text = stringResource(R.string.settings_fix_recents))
        TextBodyS(
            text = stringResource(R.string.settings_fix_recents_hint),
            color = Base70,
        )
    }
}

@Composable
private fun LabelStyleSettings(
    fontSizeSp: Int,
    color: Color,
    onFontSizeChange: (Int) -> Unit,
    onColorChange: (Color) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Orange20, RoundedCornerShape(28.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TextH4(text = stringResource(R.string.settings_label_style))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(
                    FolderBackgrounds.brushFor(FolderBackgrounds.DEFAULT_ID),
                    RoundedCornerShape(16.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            ContrastLabel(
                text = stringResource(R.string.settings_label_preview),
                fontSizeSp = fontSizeSp,
                color = color,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                maxLines = 1,
            )
        }
        TextBodyS(
            text = stringResource(R.string.settings_label_font_size) + ": $fontSizeSp",
            color = Base70,
        )
        DiscreteSlider(
            value = fontSizeSp,
            min = IconLabelStyle.MIN_FONT_SIZE_SP,
            max = IconLabelStyle.MAX_FONT_SIZE_SP,
            onValueChange = onFontSizeChange,
        )
        TextBodyS(
            text = stringResource(R.string.settings_label_color),
            color = Base70,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconLabelStyle.COLOR_PRESETS.forEach { preset ->
                val selected = colorsClose(color, preset)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(preset, CircleShape)
                        .border(
                            width = if (selected) 3.dp else 1.dp,
                            color = if (selected) Green50 else Base40,
                            shape = CircleShape,
                        )
                        .clickable { onColorChange(preset) },
                )
            }
        }
    }
}

private fun colorsClose(a: Color, b: Color): Boolean {
    return a.red == b.red && a.green == b.green && a.blue == b.blue && a.alpha == b.alpha
}

@Composable
private fun DiscreteSlider(
    value: Int,
    min: Int,
    max: Int,
    onValueChange: (Int) -> Unit,
) {
    var widthPx by remember { mutableIntStateOf(0) }
    val steps = (max - min).coerceAtLeast(1)

    fun valueFromX(x: Float) {
        if (widthPx <= 0) return
        val fraction = (x / widthPx.toFloat()).coerceIn(0f, 1f)
        onValueChange((min + (fraction * steps).roundToInt()).coerceIn(min, max))
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .onSizeChanged { widthPx = it.width }
                .pointerInput(widthPx, min, max) {
                    if (widthPx <= 0) return@pointerInput
                    detectTapGestures { offset -> valueFromX(offset.x) }
                }
                .pointerInput(widthPx, min, max) {
                    if (widthPx <= 0) return@pointerInput
                    detectDragGestures(
                        onDragStart = { offset -> valueFromX(offset.x) },
                        onDrag = { change, _ ->
                            change.consume()
                            valueFromX(change.position.x)
                        },
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Base0, RoundedCornerShape(2.dp)),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (step in min..max) {
                    val active = step <= value
                    val selected = step == value
                    Box(
                        modifier = Modifier
                            .size(if (selected) 28.dp else 18.dp)
                            .background(
                                if (active) Green50 else Base0,
                                CircleShape,
                            )
                            .then(
                                if (!active) {
                                    Modifier.border(1.dp, Base40, CircleShape)
                                } else {
                                    Modifier
                                },
                            ),
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            for (step in min..max) {
                Text(
                    text = step.toString(),
                    modifier = Modifier
                        .clickable(role = Role.Button) { onValueChange(step) }
                        .padding(8.dp),
                    fontSize = 14.sp,
                    fontWeight = if (step == value) FontWeight.Bold else FontWeight.Normal,
                    color = if (step == value) Green50 else Base40,
                )
            }
        }
    }
}
