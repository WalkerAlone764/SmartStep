package com.upsidedown.smartstep.home.presentation.step_counter.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import java.text.NumberFormat
import java.util.Locale

data class DailyStep(
    val day: String,
    val steps: Int
)

@Composable
fun DailyAverageCard(
    dailySteps: List<DailyStep>,
    averageSteps: Int,
    goalSteps: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .widthIn(
                max = 394.dp
            )
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Daily Average: ${NumberFormat.getNumberInstance(Locale.US).format(averageSteps)} steps",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dailySteps.forEach { dailyStep ->
                    DailyProgress(
                        day = dailyStep.day,
                        steps = dailyStep.steps,
                        progress = if (goalSteps > 0) (dailyStep.steps.toFloat() / goalSteps).coerceIn(0f, 1f) else 0f
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyProgress(
    day: String,
    steps: Int,
    progress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

    Column(
        modifier = modifier
        .width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Background track (White, thick)
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.size(40.dp),
                color = Color.White,
                strokeWidth = 5.dp,
            )
            // Progress arc (Green, thinner to create the "inset" look)
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.size(37.dp),
                color = Color(0xFF00E676),
                trackColor = Color.White,
                strokeWidth = 1.5.dp,
                strokeCap = StrokeCap.Round
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = day,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = NumberFormat.getNumberInstance(Locale.US).format(steps),
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun DailyAverageCardPreview() {
    val sampleDailySteps = listOf(
        DailyStep("Sun", 12345),
        DailyStep("Mon", 0),
        DailyStep("Tue", 0),
        DailyStep("Wed", 0),
        DailyStep("Thu", 0),
        DailyStep("Fri", 7890),
        DailyStep("Sat", 635)
    )
    SmartStepTheme {
        DailyAverageCard(
            dailySteps = sampleDailySteps,
            averageSteps = 5125,
            goalSteps = 15000,
            modifier = Modifier.padding(16.dp)
        )
    }
}
