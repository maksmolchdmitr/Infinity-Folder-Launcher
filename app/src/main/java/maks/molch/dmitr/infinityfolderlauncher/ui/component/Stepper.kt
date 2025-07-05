package maks.molch.dmitr.infinityfolderlauncher.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Grey50

@Composable
fun Stepper(step: Int) {
    if (step !in 1..3) {
        throw IllegalStateException("Step must be in range 1 to 3!")
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (index in 1..3) {
            Box {
                if (index == step) {
                    ActiveStep()
                } else {
                    DisabledStep()
                }
            }
        }
    }
}

@Composable
private fun DisabledStep() {
    Icon(
        modifier = Modifier.size(10.dp, 10.dp),
        imageVector = StepperItem,
        contentDescription = null,
        tint = Grey50
    )
}

@Composable
private fun ActiveStep() {
    Icon(
        modifier = Modifier.size(30.dp, 10.dp),
        imageVector = ActiveStepperItem,
        contentDescription = null,
        tint = Green50,
    )
}