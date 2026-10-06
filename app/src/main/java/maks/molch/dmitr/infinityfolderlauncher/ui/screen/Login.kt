package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.dao.OnboardingDao
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.VK
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Yandex
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.DefaultFontFamily
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.WallpaperGradientColors

@Composable
fun LoginScreen(
    screen: MutableState<Screen>,
    onboardingDao: OnboardingDao,
) {
    fun finish() {
        onboardingDao.setLoginCompleted()
        onboardingDao.setOnboardingCompleted()
        screen.value = Screen.Main
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(WallpaperGradientColors))
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(24.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.login_no_account),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Green50)
                    .clickable { finish() }
                    .padding(vertical = 18.dp),
                color = Base0,
                fontFamily = DefaultFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.login_or),
                color = Base0,
                fontFamily = DefaultFontFamily,
                fontSize = 14.sp,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SocialButton(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.login_vk),
                    onClick = { finish() },
                ) {
                    Icon(
                        imageVector = Icons.VK,
                        contentDescription = null,
                        tint = Base70,
                        modifier = Modifier.size(28.dp),
                    )
                }
                SocialButton(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.login_yandex),
                    onClick = { finish() },
                ) {
                    Icon(
                        imageVector = Icons.Yandex,
                        contentDescription = null,
                        tint = Base70,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SocialButton(
    modifier: Modifier,
    label: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Base0)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        icon()
        Text(
            text = label,
            fontFamily = DefaultFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Base70,
        )
    }
}
