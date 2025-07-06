package maks.molch.dmitr.infinityfolderlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import maks.molch.dmitr.infinityfolderlauncher.dao.OnboardingUtil
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.MainScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.OnboardingScreen
import maks.molch.dmitr.infinityfolderlauncher.ui.screen.SplashScreen

class MainActivity : ComponentActivity() {
    private val onboardingUtil by lazy { OnboardingUtil(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val screen = remember {
                mutableStateOf(Screen.Splash)
            }
            when (screen.value) {
                Screen.Main -> MainScreen()
                Screen.Splash -> SplashScreen(screen, onboardingUtil)
                Screen.Onboarding -> OnboardingScreen(screen, onboardingUtil)
            }
        }
    }
}

enum class Screen {
    Main,
    Splash,
    Onboarding,
}