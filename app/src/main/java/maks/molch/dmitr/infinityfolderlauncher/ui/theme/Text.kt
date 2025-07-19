package maks.molch.dmitr.infinityfolderlauncher.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.DeviceFontFamilyName
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

val DefaultFontFamily = FontFamily(
    Font(
        DeviceFontFamilyName("sans-serif")
    )
)

val DefaultFontStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontSize = 14.sp
)