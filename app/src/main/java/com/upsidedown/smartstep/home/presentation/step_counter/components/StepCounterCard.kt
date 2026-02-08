package com.upsidedown.smartstep.home.presentation.step_counter.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.upsidedown.smartstep.R
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StepCounterCard(
    currentSteps: Int,
    formattedCurrentSteps: String,
    goalSteps: Int,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = if (goalSteps > 0) currentSteps.toFloat() / goalSteps else 0f
    )

    Surface(
        modifier = modifier
            .widthIn(
                max = 380.dp
            )
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 4.dp,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.speed),
                    contentDescription = null
                )
            }

            Column {
                Text(
                    text = formattedCurrentSteps,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 64.sp,
                )
                Text(
                    text = stringResource(R.string.steps_count_format, goalSteps),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(2.8.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0f),
                    drawStopIndicator = {},
                    gapSize = 0.dp,
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}

@Preview
@Composable
private fun StepCounterCardPreview() {
    SmartStepTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            StepCounterCard(
                currentSteps = 4523,
                formattedCurrentSteps = "4,523",
                goalSteps = 6000
            )
        }
    }
}
