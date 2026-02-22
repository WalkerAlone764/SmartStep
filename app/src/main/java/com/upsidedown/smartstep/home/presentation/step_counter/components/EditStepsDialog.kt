package com.upsidedown.smartstep.home.presentation.step_counter.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.upsidedown.smartstep.core.presentation.designsystem.components.button.SmartStepButton
import com.upsidedown.smartstep.core.presentation.designsystem.components.button.SmartStepButtonStyle
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun EditStepsDialog(
    steps: TextFieldState,
    onDismiss: () -> Unit,
    onClickDate: () -> Unit,
    onSave: (date: String, time: String, steps: String) -> Unit,
    modifier: Modifier = Modifier,
    showTimeColumn: Boolean = false,
    date: String = "2025/11/30",
    time: String = "15:00-16:00",

) {

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp))
                .widthIn(
                    max = 328.dp
                )
                .fillMaxWidth()
                .padding(
                    24.dp
                ),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Edit steps",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Calories, distance & duration will be recalculated accordingly.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Column(
                modifier = Modifier
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SmartStepInputField(
                    label = "Date",
                    value = date,
                    onClick = onClickDate,
                    showArrow = true
                )
                
                AnimatedVisibility(showTimeColumn) {
                    SmartStepInputField(
                        label = "Time",
                        value = time,
                        onClick = { /* TODO: Show time picker */ },
                        showArrow = true
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Steps",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                            BasicTextField(
                                state = steps,
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                lineLimits = TextFieldLineLimits.SingleLine,
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SmartStepButton(
                    text = "Cancel",
                    onClick = onDismiss,
                    style = SmartStepButtonStyle.TEXT
                )
                SmartStepButton(
                    text = "Save",
                    onClick = { onSave(date, time, steps.text.toString()) },
                    style = SmartStepButtonStyle.TEXT
                )
            }
        }
    }
}

@Composable
private fun SmartStepInputField(
    label: String,
    value: String,
    onClick: () -> Unit,
    showArrow: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (showArrow) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditStepsDialogPreview() {
    SmartStepTheme {
        EditStepsDialog(
            steps = TextFieldState(initialText = "5000"),
            onClickDate = {},
            onDismiss = {},
            onSave = { _, _, _ -> },
            showTimeColumn = true
        )
    }
}

@Preview
@Composable
private fun EditStepsDialogNoTimePreview() {
    SmartStepTheme {
        EditStepsDialog(
            steps = TextFieldState(initialText = "5000"),
            onDismiss = {},
            onClickDate = {},
            onSave = { _, _, _ -> },
            showTimeColumn = false
        )
    }
}