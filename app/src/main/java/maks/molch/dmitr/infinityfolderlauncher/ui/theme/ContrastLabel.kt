package maks.molch.dmitr.infinityfolderlauncher.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

/**
 * Label with dark outline + soft shadow so any fill color stays readable
 * on light, dark and busy photo backgrounds.
 */
@Composable
fun ContrastLabel(
    text: String,
    fontSizeSp: Int,
    color: Color = Color.White,
    modifier: Modifier = Modifier,
    maxLines: Int = 4,
    fontWeight: FontWeight = FontWeight.Medium,
) {
    val size = fontSizeSp.sp
    val line = (fontSizeSp + 2).sp
    val strokeWidth = (fontSizeSp * 0.34f).coerceIn(3.2f, 7f)
    val outline = if (isLightColor(color)) Color.Black else Color.White
    val baseStyle = TextStyle(
        fontSize = size,
        fontWeight = fontWeight,
        textAlign = TextAlign.Center,
        lineHeight = line,
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            style = baseStyle.copy(
                color = outline,
                drawStyle = Stroke(
                    width = strokeWidth,
                    join = StrokeJoin.Round,
                    miter = 4f,
                ),
            ),
            softWrap = true,
            maxLines = maxLines,
            overflow = TextOverflow.Clip,
        )
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            style = baseStyle.copy(
                color = color,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.45f),
                    offset = Offset(0f, 1.2f),
                    blurRadius = 2.5f,
                ),
            ),
            softWrap = true,
            maxLines = maxLines,
            overflow = TextOverflow.Clip,
        )
    }
}

private fun isLightColor(color: Color): Boolean {
    // Relative luminance (sRGB approximation).
    val luminance = 0.2126f * color.red + 0.7152f * color.green + 0.0722f * color.blue
    return luminance >= 0.55f
}
