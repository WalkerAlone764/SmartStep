package com.upsidedown.smartstep.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsidedown.smartstep.R
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.upsidedown.smartstep.core.presentation.designsystem.theme.bodyMediumMedium
import kotlin.math.abs

@Composable
fun SmartStepPickerCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    unitSelector: @Composable () -> Unit = {},
    pickerContent: @Composable () -> Unit = {},
    onCancel: () -> Unit = {},
    onOk: () -> Unit = {}
) {
    Surface(
        modifier = modifier.width(280.dp),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            unitSelector()

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                pickerContent()
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SmartStepButton(
                    text = stringResource(R.string.cancel),
                    onClick = onCancel,
                    style = SmartStepButtonStyle.TEXT
                )
                Spacer(modifier = Modifier.width(8.dp))
                SmartStepButton(
                    text = stringResource(R.string.ok),
                    onClick = onOk,
                    style = SmartStepButtonStyle.TEXT
                )
            }
        }
    }
}

@Composable
fun VerticalNumberPicker(
    range: IntRange,
    selectedValue: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    unit: String? = null
) {
    val listState = rememberLazyListState()
    val itemHeight = 40.dp
    val visibleItemsCount = 3
    val items = remember(range) { range.toList() }

    LaunchedEffect(range, selectedValue) {
        val targetIndex = items.indexOf(selectedValue)
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
        if (currentCenterItemIndex in items.indices) {
            onValueChange(items[currentCenterItemIndex])
        }
    }

    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = itemHeight),
            modifier = Modifier.height(itemHeight * visibleItemsCount),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items.size) { index ->
                val value = items[index]
                val isSelected = value == selectedValue
                
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = value.toString(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                fontSize = if (isSelected) 18.sp else 16.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.inverseOnSurface
                            ),
                            textAlign = TextAlign.Center
                        )
                        if (unit != null && isSelected) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = unit,
                                style = MaterialTheme.typography.bodyMediumMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SmartStepHeightPicker(
    modifier: Modifier = Modifier
) {
    var isCm by remember { mutableIntStateOf(0) }
    var cmValue by remember { mutableIntStateOf(175) }
    var ftValue by remember { mutableIntStateOf(5) }
    var inValue by remember { mutableIntStateOf(9) }

    SmartStepPickerCard(
        title = stringResource(R.string.height),
        subtitle = stringResource(R.string.used_to_calculate_distance),
        modifier = modifier,
        unitSelector = {
            Row(modifier = Modifier.fillMaxWidth()) {
                SmartStepChip(
                    text = stringResource(R.string.cm),
                    isSelected = isCm == 0,
                    style = SmartStepChipStyle.LEFT,
                    onClick = { isCm = 0 },
                    modifier = Modifier.weight(1f)
                )
                SmartStepChip(
                    text = stringResource(R.string.ft_in),
                    isSelected = isCm == 1,
                    style = SmartStepChipStyle.RIGHT,
                    onClick = { isCm = 1 },
                    modifier = Modifier.weight(1f)
                )
            }
        },
        pickerContent = {
            if (isCm == 0) {
                VerticalNumberPicker(
                    range = 100..250,
                    selectedValue = cmValue,
                    onValueChange = { cmValue = it },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Row(modifier = Modifier.fillMaxWidth()) {
                    VerticalNumberPicker(
                        range = 1..8,
                        selectedValue = ftValue,
                        onValueChange = { ftValue = it },
                        modifier = Modifier.weight(1f),
                        unit = stringResource(R.string.ft)
                    )
                    VerticalNumberPicker(
                        range = 0..11,
                        selectedValue = inValue,
                        onValueChange = { inValue = it },
                        modifier = Modifier.weight(1f),
                        unit = stringResource(R.string.in_unit)
                    )
                }
            }
        }
    )
}

@Composable
fun SmartStepWeightPicker(
    modifier: Modifier = Modifier
) {
    var isKg by remember { mutableIntStateOf(0) }
    var kgValue by remember { mutableIntStateOf(65) }
    var lbsValue by remember { mutableIntStateOf(143) }

    SmartStepPickerCard(
        title = stringResource(R.string.weight),
        subtitle = stringResource(R.string.used_to_calculate_calories),
        modifier = modifier,
        unitSelector = {
            Row(modifier = Modifier.fillMaxWidth()) {
                SmartStepChip(
                    text = stringResource(R.string.kg),
                    isSelected = isKg == 0,
                    style = SmartStepChipStyle.LEFT,
                    onClick = { isKg = 0 },
                    modifier = Modifier.weight(1f)
                )
                SmartStepChip(
                    text = stringResource(R.string.lbs),
                    isSelected = isKg == 1,
                    style = SmartStepChipStyle.RIGHT,
                    onClick = { isKg = 1 },
                    modifier = Modifier.weight(1f)
                )
            }
        },
        pickerContent = {
            if (isKg == 0) {
                VerticalNumberPicker(
                    range = 30..200,
                    selectedValue = kgValue,
                    onValueChange = { kgValue = it },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                VerticalNumberPicker(
                    range = 70..450,
                    selectedValue = lbsValue,
                    onValueChange = { lbsValue = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun HeightPickerCmPreview() {
    SmartStepTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SmartStepHeightPicker()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeightPickerPreview() {
    SmartStepTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SmartStepWeightPicker()
        }
    }
}

@Preview(showBackground = true, device = DESKTOP)
@Composable
private fun PickerComparisonPreview() {
    SmartStepTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SmartStepHeightPicker(
                modifier = Modifier
                    .weight(1f)
            )
            SmartStepWeightPicker(
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}
