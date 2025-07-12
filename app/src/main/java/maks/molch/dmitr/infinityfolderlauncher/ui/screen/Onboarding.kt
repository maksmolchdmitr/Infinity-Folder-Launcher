package maks.molch.dmitr.infinityfolderlauncher.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.MainActivity
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.Screen
import maks.molch.dmitr.infinityfolderlauncher.dao.OnboardingDao
import maks.molch.dmitr.infinityfolderlauncher.ui.component.Stepper
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.ArrowIcon
import maks.molch.dmitr.infinityfolderlauncher.ui.custom.Icons
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base60
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base90
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.DefaultFontFamily
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.DescriptorBackgroundColor
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.InfinityFolderLauncherTheme

@Preview
@Composable
fun PreviewOnboardingScreen() {
    val mainActivity = MainActivity()
    val screen = remember {
        mutableStateOf(Screen.Onboarding)
    }
    OnboardingScreen(screen, OnboardingDao(mainActivity))
}

@Composable
fun OnboardingScreen(screen: MutableState<Screen>, onboardingDao: OnboardingDao) {
    InfinityFolderLauncherTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(R.drawable.infinity_folder_logo),
                    contentScale = ContentScale.Crop,
                )
                .padding(horizontal = 16.dp, vertical = 16.dp),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Descriptor(screen, onboardingDao)
        }
    }
}

@Composable
fun Descriptor(screen: MutableState<Screen>, onboardingDao: OnboardingDao) {
    Column(
        modifier = Modifier
            .height(259.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(DescriptorBackgroundColor, DescriptorBackgroundColor)
                ),
                shape = RoundedCornerShape(28.dp),
            )
            .padding(16.dp, 24.dp, 16.dp, 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val step = descriptorStepper()
        DescriptorTextBlock(step)
        DescriptorButtonRow(step, screen, onboardingDao)
    }
}

@Composable
private fun descriptorStepper(): MutableIntState {
    val step = remember {
        mutableIntStateOf(1)
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Stepper(step.intValue)
    }
    return step
}

@Composable
fun DescriptorTextBlock(step: MutableIntState) {
    val stepToTextPair: Map<Int, Pair<String, String>> = mapOf(
        1 to ("Бесконечные папки" to "Создавай папку и в ней ещё много папок!"),
        2 to ("Минимализм" to "Функционал без лишних деталей"),
        3 to ("Виджеты" to "Поддержка различных функциональных виджетов"),
    )
    Column(
        modifier = Modifier.height(125.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stepToTextPair[step.intValue]?.first ?: "",
            fontFamily = DefaultFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = Base90,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stepToTextPair[step.intValue]?.second ?: "",
            fontFamily = DefaultFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Base60,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun DescriptorButtonRow(
    step: MutableIntState,
    screen: MutableState<Screen>,
    onboardingDao: OnboardingDao,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (step.intValue != 3) {
            IconButton(
                modifier = Modifier.fillMaxHeight(),
                onClick = { finish(screen, onboardingDao) }
            ) {
                Text("Skip")
            }
        }
        IconButton(
            modifier = Modifier
                .background(Green50, shape = RoundedCornerShape(12.dp)),
            onClick = {
                if (step.intValue != 1) {
                    step.intValue--
                }
                println("Step = ${step.intValue}")
            },
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    modifier = Modifier.rotate(180f),
                    imageVector = Icons.ArrowIcon,
                    contentDescription = null,
                    tint = Base0
                )
            }
        }
        IconButton(
            modifier = Modifier
                .background(Green50, shape = RoundedCornerShape(12.dp))
                .fillMaxSize(),
            onClick = {
                if (step.intValue == 3) {
                    finish(screen, onboardingDao)
                }
                step.intValue++
                println("Step = ${step.intValue}")
            },
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (step.intValue == 3) {
                    Text(
                        "Let's go",
                        fontFamily = DefaultFontFamily,
                        fontSize = 16.sp,
                        color = Base0,
                    )
                }
                Icon(
                    imageVector = Icons.ArrowIcon,
                    contentDescription = null,
                    tint = Base0
                )
            }
        }
    }
}

private fun finish(
    screen: MutableState<Screen>,
    onboardingDao: OnboardingDao
) {
    screen.value = Screen.Main
    onboardingDao.setOnboardingCompleted()
}