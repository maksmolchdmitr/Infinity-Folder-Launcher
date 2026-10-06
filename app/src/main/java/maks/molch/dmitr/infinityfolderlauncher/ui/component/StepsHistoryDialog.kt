package maks.molch.dmitr.infinityfolderlauncher.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maks.molch.dmitr.infinityfolderlauncher.InfinityFolderApp
import maks.molch.dmitr.infinityfolderlauncher.R
import maks.molch.dmitr.infinityfolderlauncher.dao.DaySteps
import maks.molch.dmitr.infinityfolderlauncher.dao.StepsDao
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextBodyS
import maks.molch.dmitr.infinityfolderlauncher.ui.component.common.TextH4
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base0
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base40
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Base70
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Green50
import maks.molch.dmitr.infinityfolderlauncher.ui.theme.Orange10
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun StepsHistoryDialog(
    dailyGoal: Int,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val stepsDao = (context.applicationContext as InfinityFolderApp).stepsDao
    val historyVersion by stepsDao.historyVersion.collectAsState()
    val stepsToday by stepsDao.stepsToday.collectAsState()
    val days = remember(historyVersion, stepsToday) {
        stepsDao.history(StepsDao.HISTORY_DAYS)
    }
    val goal = dailyGoal.coerceAtLeast(1)
    val numberFormat = remember { NumberFormat.getIntegerInstance(Locale.getDefault()) }
    val maxSteps = remember(days) {
        days.maxOfOrNull { it.steps }?.coerceAtLeast(goal) ?: goal
    }

    Column(
        modifier = Modifier
            .width(328.dp)
            .background(Orange10, RoundedCornerShape(28.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        TextH4(text = stringResource(R.string.steps_history_title))
        TextBodyS(
            text = stringResource(
                R.string.steps_history_today,
                numberFormat.format(stepsToday),
            ),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            days.forEachIndexed { index, day ->
                val fraction = (day.steps.toFloat() / maxSteps).coerceIn(0f, 1f)
                val isToday = index == days.lastIndex
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Text(
                        text = if (day.steps > 0) numberFormat.format(day.steps) else "·",
                        color = Base40,
                        fontSize = 9.sp,
                        maxLines = 1,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(88.dp),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .fillMaxHeight(fraction.coerceAtLeast(0.04f))
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(if (isToday) Green50 else Base50.copy(alpha = 0.55f)),
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dayLabel(day),
                        color = if (isToday) Base70 else Base40,
                        fontSize = 11.sp,
                        fontWeight = if (isToday) FontWeight.SemiBold else FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            days.asReversed().forEach { day ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = dayListLabel(day),
                        color = Base70,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = stringResource(
                            R.string.steps_history_row,
                            numberFormat.format(day.steps),
                        ),
                        color = Base40,
                        fontSize = 14.sp,
                    )
                }
            }
        }

        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .background(Base50, RoundedCornerShape(12.dp)),
        ) {
            TextBodyS(
                text = stringResource(R.string.close),
                color = Base0,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun dayLabel(day: DaySteps): String {
    val todayKey = rememberTodayKey()
    val yesterdayKey = rememberYesterdayKey()
    return when (day.dayKey) {
        todayKey -> stringResource(R.string.steps_history_today_short)
        yesterdayKey -> stringResource(R.string.steps_history_yesterday_short)
        else -> formatWeekday(day.dayKey)
    }
}

@Composable
private fun dayListLabel(day: DaySteps): String {
    val todayKey = rememberTodayKey()
    val yesterdayKey = rememberYesterdayKey()
    return when (day.dayKey) {
        todayKey -> stringResource(R.string.steps_history_today_label)
        yesterdayKey -> stringResource(R.string.steps_history_yesterday_label)
        else -> formatFullDay(day.dayKey)
    }
}

@Composable
private fun rememberTodayKey(): String = remember {
    SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Calendar.getInstance().time)
}

@Composable
private fun rememberYesterdayKey(): String = remember {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -1)
    SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
}

private fun formatWeekday(dayKey: String): String {
    val parsed = runCatching {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dayKey)
    }.getOrNull() ?: return dayKey
    return SimpleDateFormat("EE", Locale.forLanguageTag("ru")).format(parsed)
        .replaceFirstChar { it.uppercaseChar() }
        .take(2)
}

private fun formatFullDay(dayKey: String): String {
    val parsed = runCatching {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dayKey)
    }.getOrNull() ?: return dayKey
    return SimpleDateFormat("d MMM", Locale.forLanguageTag("ru")).format(parsed)
}
