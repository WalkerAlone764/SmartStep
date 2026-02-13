package com.upsidedown.smartstep.home.presentation.step_counter.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsidedown.smartstep.R
import com.upsidedown.smartstep.core.presentation.designsystem.components.button.SmartStepButton
import com.upsidedown.smartstep.core.presentation.designsystem.components.button.SmartStepButtonStyle
import com.upsidedown.smartstep.core.presentation.designsystem.components.layout.DialogLayout
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import kotlin.math.abs

@Composable
fun SelectStepGoalDialog(
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit,
    modifier: Modifier = Modifier,
    initialValue: Int = 6000,
    values: List<Int> = (1..40).map { it * 1000 }.reversed()
) {
    var selectedValue by rememberSaveable { mutableIntStateOf(initialValue) }

    DialogLayout(
        onDismiss = onDismiss,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Step Goal",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            StepGoalPicker(
                values = values,
                selectedValue = selectedValue,
                onValueChange = { selectedValue = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            SmartStepButton(
                text = stringResource(R.string.save),
                onClick = { onSave(selectedValue) },
                modifier = Modifier.fillMaxWidth()
            )

            SmartStepButton(
                text = stringResource(R.string.cancel),
                onClick = onDismiss,
                style = SmartStepButtonStyle.TEXT,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun StepGoalPicker(
    values: List<Int>,
    selectedValue: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val itemHeight = 48.dp
    val visibleItemsCount = 5 // Odd number ensures a centered item

    LaunchedEffect(values, selectedValue) {
        val targetIndex = values.indexOf(selectedValue)
        if (targetIndex != -1 && !listState.isScrollInProgress) {
            listState.scrollToItem(targetIndex)
        }
    }

    val currentCenterItemIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            layoutInfo.visibleItemsInfo.minByOrNull {
                abs((it.offset + it.size / 2) - center)
            }?.index ?: 0
        }
    }

    LaunchedEffect(currentCenterItemIndex) {
        if (currentCenterItemIndex in values.indices && listState.isScrollInProgress) {
            onValueChange(values[currentCenterItemIndex])
        }
    }

    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    Box(
        modifier = modifier.height(itemHeight * visibleItemsCount),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(12.dp)
                )
        )
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = itemHeight * 2), // Correct padding for 5 visible items
            modifier = Modifier.matchParentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(values) { index, value ->
                val isSelected = value == selectedValue

                val fontSize by animateFloatAsState(
                    targetValue = if (isSelected) 22f else 18f,
                    label = "fontSize"
                )
                val fontWeight by animateIntAsState(
                    targetValue = if (isSelected) FontWeight.Medium.weight else FontWeight.Normal.weight,
                    label = "fontWeight"
                )
                val color by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    label = "color"
                )

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = value.toString(),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight(fontWeight),
                            fontSize = fontSize.sp,
                            color = color
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SmartStepTheme {
        SelectStepGoalDialog(
            onDismiss = {},
            onSave = {}
        )
    }
}
