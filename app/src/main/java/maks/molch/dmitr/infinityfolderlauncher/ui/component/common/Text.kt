package maks.molch.dmitr.infinityfolderlauncher.ui.component.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base100
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.DefaultFontFamily

@Composable
fun TextH4(
    text: String,
    color: Color = Base100,
    textAlign: TextAlign = TextAlign.Left
) {
    Text(
        text = text,
        fontFamily = DefaultFontFamily,
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium,
        color = color,
        textAlign = textAlign,
    )
}

@Composable
fun TextBodyS(
    text: String,
    color: Color = Base100,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    Text(
        text = text,
        fontFamily = DefaultFontFamily,
        fontSize = 14.sp,
        fontWeight = fontWeight,
        color = color
    )
}