package maks.molch.dmitr.infinityfolderlauncher.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.MainActivity
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.dao.putFolderName
import maks.molch.dmitr.infinityfolderlauncher.data.Application
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.DrawableImage
import maks.molch.dmitr.infinityfolderlauncher.ui.component.custom.Image


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ObjectCell(
    context: Context,
    launcherObject: LauncherObject,
    editModeEnabled: MutableState<Boolean>,
    state: MutableState<ObjectCellState> = remember { mutableStateOf(ObjectCellState.Default) },
    selectedObjectNames: MutableState<MutableSet<String>>,
    objectsWasSelected: MutableState<Boolean>,
) {
    val packageManager: PackageManager = context.packageManager
    val boxSize: MutableState<IntSize> = remember { mutableStateOf(IntSize(1, 1)) }

    Box(
        modifier = Modifier
            .onGloballyPositioned { layoutCoordinates ->
                boxSize.value = layoutCoordinates.size
            }
            .combinedClickable(
                onClick = {
                    if (editModeEnabled.value) {
                        state.value = if (state.value == ObjectCellState.SelectionBlank) {
                            selectedObjectNames.value.add(launcherObject.name)
                            objectsWasSelected.value = true
                            println("Selected object names: $selectedObjectNames")
                            ObjectCellState.SelectionMarked
                        } else {
                            selectedObjectNames.value.remove(launcherObject.name)
                            if (selectedObjectNames.value.isEmpty()) {
                                objectsWasSelected.value = false
                            }
                            println("Selected object names: $selectedObjectNames")
                            ObjectCellState.SelectionBlank
                        }
                        return@combinedClickable
                    }

                    when (launcherObject) {
                        is Application -> {
                            packageManager.getLaunchIntentForPackage(launcherObject.packageName)
                                ?.let { intent ->
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                }
                        }

                        is Folder -> {
                            val intent = Intent(context, MainActivity::class.java)
                            intent.putFolderName(launcherObject.name)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        }
                    }
                },
            )
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            when (launcherObject) {
                is Application -> {
                    DrawableImage(
                        modifier = Modifier
                            .size(70.dp, 70.dp),
                        drawable = launcherObject.getIcon(packageManager)
                    )
                }

                is Folder -> {
                    Image(
                        modifier = Modifier
                            .size(70.dp, 70.dp),
                        resourceId = R.drawable.infinity_folder_logo,
                    )
                }
            }
            Text(
                modifier = Modifier.height(14.dp),
                text = launcherObject.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
        }
    }

    if (editModeEnabled.value) {
        ObjectCellIcons(boxSize, state)
    }
}

enum class ObjectCellState {
    Default,
    Clear,
    SelectionBlank,
    SelectionMarked,
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun ObjectCellIcons(boxSize: MutableState<IntSize>, state: MutableState<ObjectCellState>) {
    val rightUp = Modifier
        .absoluteOffset(
            (35 * boxSize.value.width / 186).dp,
            (-12 * boxSize.value.width / 186).dp,
        )
        .size((24 * boxSize.value.width / 186).dp)
    val leftUp = Modifier
        .absoluteOffset(
            -(35 * boxSize.value.width / 186).dp,
            (-12 * boxSize.value.width / 186).dp,
        )
        .size((24 * boxSize.value.width / 186).dp)
    val rightDown = Modifier
        .absoluteOffset(
            (35 * boxSize.value.width / 186).dp,
            (58 * boxSize.value.height / 221).dp,
        )
        .size((24 * boxSize.value.width / 186).dp)
    val leftDown = Modifier
        .absoluteOffset(
            -(35 * boxSize.value.width / 186).dp,
            (58 * boxSize.value.height / 221).dp,
        )
        .size((24 * boxSize.value.width / 186).dp)

    when (state.value) {
        ObjectCellState.SelectionBlank -> {
            Image(leftDown, R.drawable.selection_blank_icon)
        }

        ObjectCellState.SelectionMarked -> {
            Image(leftDown, R.drawable.selection_marked_icon)
        }

        ObjectCellState.Default -> {}

        else -> TODO()
    }
}
