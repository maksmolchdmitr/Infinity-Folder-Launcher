package maks.molch.dmitr.infinityfolderlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import maks.molch.dmitr.infinityfolderlauncher.dao.ApplicationDao
import maks.molch.dmitr.infinityfolderlauncher.dao.FolderDao
import maks.molch.dmitr.infinityfolderlauncher.dao.OnboardingDao
import maks.molch.dmitr.infinityfolderlauncher.dao.SettingsDao
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.AddApplication
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.AddFolder
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.AddWidgetScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.MainScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.OnboardingScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.SettingsScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.SplashScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.InfinityFolderLauncherTheme
import maks.molch.dmitr.infinityfolderlauncher.utils.MAIN_FOLDER_ID

class MainActivity : ComponentActivity() {
    private val onboardingDao by lazy { OnboardingDao(this) }
    private val applicationDao by lazy { ApplicationDao(this) }
    private val folderDao by lazy { FolderDao(this) }
    private val settingsDao by lazy { SettingsDao(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            InfinityFolderLauncherTheme {
                val screen = remember { mutableStateOf(Screen.Splash) }
                val folderStack = remember { mutableStateListOf(MAIN_FOLDER_ID) }
                val currentFolderId = folderStack.last()

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
                        screen.value == Screen.AddWidget ||
                        screen.value == Screen.Settings,
                ) {
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
                    )

                    Screen.Splash -> SplashScreen(screen, onboardingDao)
                    Screen.Onboarding -> OnboardingScreen(screen, onboardingDao)
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

                    Screen.AddWidget -> AddWidgetScreen(
                        screen,
                        currentFolderId,
                        folderDao,
                    )

                    Screen.Settings -> SettingsScreen(screen, settingsDao)
                }
            }
        }
    }
}

enum class Screen {
    Main,
    Splash,
    Onboarding,
    AddApplication,
    AddFolder,
    AddWidget,
    Settings,
}
