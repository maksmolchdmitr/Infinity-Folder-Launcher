package maks.molch.dmitr.infinityfolderlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import maks.molch.dmitr.infinityfolderlauncher.dao.ApplicationDao
import maks.molch.dmitr.infinityfolderlauncher.dao.OnboardingDao
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.MainScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.OnboardingScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.SplashScreen

class MainActivity : ComponentActivity() {
    private val onboardingDao by lazy { OnboardingDao(this) }
    private val applicationDao by lazy { ApplicationDao(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val screen = remember {
                mutableStateOf(Screen.Splash)
            }
            when (screen.value) {
                Screen.Main -> MainScreen(this, applicationDao)
                Screen.Splash -> SplashScreen(screen, onboardingDao)
                Screen.Onboarding -> OnboardingScreen(screen, onboardingDao)
            }
        }
    }
}

enum class Screen {
    Main,
    Splash,
    Onboarding,
}