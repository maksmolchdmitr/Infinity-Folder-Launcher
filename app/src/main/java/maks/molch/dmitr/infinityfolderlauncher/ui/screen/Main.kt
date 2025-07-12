package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.MainActivity
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.dao.ApplicationDao
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.dao.putFolderName
import maks.molch.dmitr.infinityfolderlauncher.data.Application
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.data.LauncherObject
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.DrawableImage

@Composable
fun MainScreen(
    context: Context,
    folderName: String,
    folderDao: FolderDao,
    applicationDao: ApplicationDao
) {
    val objectNumberOnTheRow = 4

    val launcherObjects: List<LauncherObject> =
        listOf(Folder("Infinity Folder")) +
                (folderDao.getFolderByName(folderName)?.launcherObjects
                    ?: if (folderName == "MAIN_FOLDER") {
                        applicationDao.getInstalledApplications()
                    } else listOf())
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(R.drawable.infinity_folder_logo),
                contentScale = ContentScale.Crop,
            ),
        contentAlignment = Alignment.Center,
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(16.dp),
            columns = GridCells.Fixed(objectNumberOnTheRow),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            items(launcherObjects) {
                ObjectCell(context, it)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ObjectCell(context: Context, launcherObject: LauncherObject) {
    val packageManager: PackageManager = context.packageManager
    Box(
        modifier = Modifier
            .combinedClickable(
                onClick = {
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
                        painter = painterResource(R.drawable.infinity_folder_logo),
                        contentDescription = null
                    )
                }
            }
            Text(
                modifier = Modifier.height(42.dp),
                text = launcherObject.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
        }
    }
}