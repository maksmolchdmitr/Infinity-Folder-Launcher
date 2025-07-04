package maks.molch.dmitr.infinityfolderlauncher

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.InfinityFolderLauncherTheme
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.LogoSize

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfinityFolderLauncherTheme {
                SplashScreen()
            }
        }
    }

    @Preview
    @Composable
    private fun SplashScreen() {
        LaunchedEffect(key1 = true) {
            delay(1_000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
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
}