package com.upsidedown.smartstep.home.presentation.step_counter.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsidedown.smartstep.R
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun StepCounterCard(
    currentSteps: Int,
    formattedCurrentSteps: String,
    goalSteps: Int,
    isPaused: Boolean,
    distanceKm: Double,
    calories: Int,
    timeMin: Int,
    onEditClick: () -> Unit,
    onPausePlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = if (goalSteps > 0) currentSteps.toFloat() / goalSteps else 0f,
        label = "steps_progress"
    )

    Surface(
        modifier = modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.speed),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconButton(
                        icon = ImageVector.vectorResource(R.drawable.ic_edit),
                        onClick = onEditClick
                    )
                    IconButton(
                        icon = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        onClick = onPausePlayClick
                    )
                }
            }

            // Steps section
            Column {
                Text(
                    text = formattedCurrentSteps,
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 72.sp,
                    color = if (isPaused) Color.White.copy(alpha = 0.3f) else Color.White
                )
                Text(
                    // Changed R.string.paused to R.string.step_counter_paused to resolve resource resolution issues in Preview
                    text = if (isPaused) stringResource(R.string.step_counter_paused) else stringResource(R.string.steps_count_format, goalSteps),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f)
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

            // Bottom stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    icon = ImageVector.vectorResource(R.drawable.location_direction),
                    value = distanceKm.toString(),
                    unit = stringResource(R.string.km)
                )
                StatItem(
                    icon = ImageVector.vectorResource(R.drawable.ic_calories),
                    value = calories.toString(),
                    unit = stringResource(R.string.kcal)
                )
                StatItem(
                    icon = ImageVector.vectorResource(R.drawable.ic_clock),
                    value = timeMin.toString(),
                    unit = stringResource(R.string.min)
                )
            }
        }
    }
}

@Composable
private fun IconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .background(
                color = Color.White.copy(alpha = 0.15f),
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.White
        )
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = unit,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.alignByBaseline()
            )
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
                goalSteps = 6000,
                isPaused = false,
                distanceKm = 3.2,
                calories = 215,
                timeMin = 42,
                onEditClick = {},
                onPausePlayClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun StepCounterCardPausedPreview() {
    SmartStepTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            StepCounterCard(
                currentSteps = 4523,
                formattedCurrentSteps = "4,523",
                goalSteps = 6000,
                isPaused = true,
                distanceKm = 3.2,
                calories = 215,
                timeMin = 42,
                onEditClick = {},
                onPausePlayClick = {}
            )
        }
    }
}
