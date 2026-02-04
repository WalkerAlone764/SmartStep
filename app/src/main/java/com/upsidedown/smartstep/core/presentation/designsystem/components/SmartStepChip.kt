package com.upsidedown.smartstep.core.presentation.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsidedown.smartstep.R
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.upsidedown.smartstep.core.presentation.designsystem.theme.bodyMediumMedium


enum class SmartStepChipStyle {
    LEFT,
    RIGHT,
}
@Composable
fun SmartStepChip(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    style: SmartStepChipStyle = SmartStepChipStyle.LEFT,
    onClick: () -> Unit = {},
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.surfaceContainer
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val border = if (!isSelected) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    } else {
        null
    }

    val shape = when(style) {
        SmartStepChipStyle.LEFT -> RoundedCornerShape(
            topStart = 32.dp,
            bottomStart = 32.dp,
            topEnd = 0.dp,
            bottomEnd = 0.dp
        )
        SmartStepChipStyle.RIGHT -> RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 0.dp,
            topEnd = 32.dp,
            bottomEnd = 32.dp
        )
    }

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = backgroundColor,
        contentColor = contentColor,
        border = border
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isSelected) {
                CheckIcon(
                    color = contentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMediumMedium
            )
        }
    }
}

@Composable
private fun CheckIcon(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val path = Path().apply {
            moveTo(width * 0.25f, height * 0.5f)
            lineTo(width * 0.45f, height * 0.7f)
            lineTo(width * 0.75f, height * 0.3f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
fun LabelSelectionGrid(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SmartStepChip(
                text = stringResource(R.string.label),
                isSelected = false,
                modifier = Modifier.weight(1f)
            )
            SmartStepChip(
                text = stringResource(R.string.label),
                style = SmartStepChipStyle.RIGHT,
                isSelected = false,

                modifier = Modifier.weight(1f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SmartStepChip(
                text = stringResource(R.string.label),
                isSelected = true,
                modifier = Modifier.weight(1f)
            )
            SmartStepChip(
                text = stringResource(R.string.label),
                isSelected = true,
                style = SmartStepChipStyle.RIGHT,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LabelSelectionGridPreview() {
    SmartStepTheme {
        LabelSelectionGrid(
            modifier = Modifier.padding(16.dp)
        )
    }
}
