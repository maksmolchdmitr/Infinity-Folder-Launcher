package maks.molch.dmitr.infinityfolderlauncher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import maks.molch.dmitr.infinityfolderlauncher.data.Folder
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.AddApplication
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.AddFolder
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.AddWidgetScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.LoginScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.MainScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.OnboardingScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.SettingsScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.SplashScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.IconLabelStyle
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.InfinityFolderLauncherTheme
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.LocalIconLabelStyle
import maks.molch.dmitr.infinityfolderlauncher.utils.MAIN_FOLDER_ID
import maks.molch.dmitr.infinityfolderlauncher.widget.LauncherWidgetController

val LocalWidgetController = staticCompositionLocalOf<LauncherWidgetController?> { null }

private const val TAG = "IFL_Home"

class MainActivity : ComponentActivity() {
    private val app get() = application as InfinityFolderApp
    private val onboardingDao get() = app.onboardingDao
    private val applicationDao get() = app.applicationDao
    private val folderDao get() = app.folderDao
    private val settingsDao get() = app.settingsDao

    private val homeTicks = MutableStateFlow(0)
    lateinit var widgetController: LauncherWidgetController
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate intent=$intent cats=${intent?.categories}")

        widgetController = LauncherWidgetController(this, folderDao, settingsDao).also {
            it.register()
        }

        enableEdgeToEdge()
        setContent {
            InfinityFolderLauncherTheme {
                val labelFontSizeSp by settingsDao.labelFontSizeSp.collectAsState()
                val labelColorArgb by settingsDao.labelColorArgb.collectAsState()
                val iconLabelStyle = remember(labelFontSizeSp, labelColorArgb) {
                    IconLabelStyle(
                        fontSizeSp = labelFontSizeSp,
                        color = Color(labelColorArgb),
                    )
                }
                CompositionLocalProvider(
                    LocalWidgetController provides widgetController,
                    LocalIconLabelStyle provides iconLabelStyle,
                ) {
                    val screen = remember { mutableStateOf(Screen.Splash) }
                    val folderStack = remember { mutableStateListOf(MAIN_FOLDER_ID) }
                    val currentFolderId = folderStack.last()
                    val folderToEdit = remember { mutableStateOf<Folder?>(null) }
                    val homeTick by homeTicks.collectAsState()
                    val homeReset = remember { mutableStateOf(0) }

                    LaunchedEffect(homeTick) {
                        if (homeTick == 0) return@LaunchedEffect
                        folderToEdit.value = null
                        folderStack.clear()
                        folderStack.add(MAIN_FOLDER_ID)
                        if (screen.value != Screen.Splash &&
                            screen.value != Screen.Onboarding &&
                            screen.value != Screen.Login
                        ) {
                            screen.value = Screen.Main
                        }
                        homeReset.value = homeTick
                    }

                    BackHandler(enabled = folderStack.size > 1 && screen.value == Screen.Main) {
                        folderStack.removeAt(folderStack.lastIndex)
                    }
                    BackHandler(
                        enabled = folderStack.size == 1 &&
                            (screen.value == Screen.Main || screen.value == Screen.Splash),
                    ) {
                    }
                    BackHandler(
                        enabled = screen.value == Screen.AddApplication ||
                            screen.value == Screen.AddFolder ||
                            screen.value == Screen.EditFolder ||
                            screen.value == Screen.AddWidget ||
                            screen.value == Screen.Settings,
                    ) {
                        folderToEdit.value = null
                        screen.value = Screen.Main
                    }

                    when (screen.value) {
                        Screen.Main -> MainScreen(
                            context = this,
                            screen = screen,
                            folderStack = folderStack,
                            currentFolderId = currentFolderId,
                            folderDao = folderDao,
                            settingsDao = settingsDao,
                            homeReset = homeReset.value,
                            onEditFolder = { folder ->
                                folderToEdit.value = folderDao.getById(folder.id) ?: folder
                                screen.value = Screen.EditFolder
                            },
                        )

                        Screen.Splash -> SplashScreen(screen, onboardingDao)
                        Screen.Onboarding -> OnboardingScreen(screen, onboardingDao)
                        Screen.Login -> LoginScreen(screen, onboardingDao)
                        Screen.AddApplication -> AddApplication(
                            this,
                            screen,
                            applicationDao,
                            currentFolderId,
                            folderDao,
                            settingsDao,
                        )

                        Screen.AddFolder -> AddFolder(
                            screen,
                            currentFolderId,
                            folderDao,
                        )

                        Screen.EditFolder -> AddFolder(
                            screen = screen,
                            currentFolderId = currentFolderId,
                            folderDao = folderDao,
                            folderToEdit = folderToEdit.value,
                            onEditDone = { folderToEdit.value = null },
                        )

                        Screen.AddWidget -> AddWidgetScreen(
                            screen = screen,
                            currentFolderId = currentFolderId,
                            folderDao = folderDao,
                            onPickSystemWidget = {
                                widgetController.startPick(currentFolderId)
                                screen.value = Screen.Main
                            },
                        )

                        Screen.Settings -> SettingsScreen(screen, settingsDao)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
        if (::widgetController.isInitialized) {
            widgetController.startListening()
        }
        app.stepsDao.start()
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
        app.stepsDao.start()
        // Do not kill com.miui.home here. On Android 14+ killBackgroundProcesses() is a
        // no-op for other UIDs, and TouchInteractionService keeps miui.home alive anyway.
        // Killing it in onPause (previous bug) destroyed Recents while it was opening.
    }

    override fun onPause() {
        // Recents on HyperOS runs inside com.miui.home. Pausing us is normal when overview
        // opens — never touch that package from here.
        Log.i(TAG, "onPause isFinishing=$isFinishing")
        super.onPause()
    }

    override fun onStop() {
        Log.i(TAG, "onStop")
        if (::widgetController.isInitialized) {
            widgetController.stopListening()
        }
        app.stepsDao.stop()
        super.onStop()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        Log.i(TAG, "onUserLeaveHint (home/recents/app switch likely)")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        Log.i(TAG, "onNewIntent action=${intent.action} cats=${intent.categories}")
        if (intent.hasCategory(Intent.CATEGORY_HOME)) {
            homeTicks.value = homeTicks.value + 1
        }
    }
}

enum class Screen {
    Main,
    Splash,
    Onboarding,
    Login,
    AddApplication,
    AddFolder,
    EditFolder,
    AddWidget,
    Settings,
}
