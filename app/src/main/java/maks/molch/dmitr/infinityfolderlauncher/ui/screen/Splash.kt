package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.dao.OnboardingUtil
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.LogoSize

@Composable
fun SplashScreen(screen: MutableState<Screen>, onboardingUtil: OnboardingUtil) {
    LaunchedEffect(key1 = true) {
        delay(1_000)
        screen.value = if (onboardingUtil.onboardingIsCompleted()) {
            Screen.Main
        } else {
            Screen.Onboarding
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Green50),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.infinity_folder_logo),
            contentDescription = null,
            modifier = Modifier.size(LogoSize, LogoSize),
            alignment = Alignment.Center
        )
    }
}