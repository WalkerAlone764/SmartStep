package com.upsidedown.smartstep.core.presentation.designsystem.components.picker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
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
import com.upsidedown.smartstep.core.presentation.designsystem.components.button.SmartStepButton
import com.upsidedown.smartstep.core.presentation.designsystem.components.button.SmartStepButtonStyle
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.upsidedown.smartstep.core.presentation.designsystem.theme.bodyMediumMedium
import com.upsidedown.smartstep.core.presentation.util.UiText
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
        modifier = modifier
            .widthIn(
                max = 328.dp
            )
            .width(280.dp),
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
            Spacer(modifier = Modifier.height(4.dp))
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
                    .height(120.dp),
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

            Spacer(modifier = Modifier.height(18.dp))

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
        if (currentCenterItemIndex in items.indices && listState.isScrollInProgress) {
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

                val fontSize by animateFloatAsState(
                    targetValue = if (isSelected) 18f else 16f,
                    label = "fontSize"
                )
                val fontWeight by animateIntAsState(
                    targetValue = if (isSelected) FontWeight.Medium.weight else FontWeight.Normal.weight,
                    label = "fontWeight"
                )
                val color by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.inverseOnSurface,
                    label = "color"
                )
                
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .animateContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
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


sealed class SmartStepHeightType(
    val title: UiText
) {
    data class CM(val selectedValue: Int = 175): SmartStepHeightType(UiText.StringResource(R.string.cm))
    data class FtInch(val selectedFt: Int = 5, val selectedInch: Int = 9): SmartStepHeightType(UiText.StringResource(R.string.ft_in))
}

@Composable
fun SmartStepHeightPicker(
    selectedType: SmartStepHeightType,
    onChange: (type: SmartStepHeightType) -> Unit,
    modifier: Modifier = Modifier,
) {

    SmartStepPickerCard(
        title = stringResource(R.string.height),
        subtitle = stringResource(R.string.used_to_calculate_distance),
        modifier = modifier,
        unitSelector = {
            Row(modifier = Modifier.fillMaxWidth()) {
                SmartStepChip(
                    text = stringResource(R.string.cm),
                    isSelected = selectedType is SmartStepHeightType.CM,
                    style = SmartStepChipStyle.LEFT,
                    onClick = {
                        onChange(SmartStepHeightType.CM())
                    },
                    modifier = Modifier.weight(1f)
                )
                SmartStepChip(
                    text = stringResource(R.string.ft_in),
                    isSelected = selectedType is SmartStepHeightType.FtInch,
                    style = SmartStepChipStyle.RIGHT,
                    onClick = {
                        onChange(SmartStepHeightType.FtInch())
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        },
        pickerContent = {
            val typeKey = when (selectedType) {
                is SmartStepHeightType.CM -> 0
                is SmartStepHeightType.FtInch -> 1
            }

            AnimatedContent(
                targetState = typeKey,
                transitionSpec = {
                    (fadeIn() + scaleIn()).togetherWith(fadeOut() + scaleOut())
                },
                label = "HeightPickerAnimation"
            ) { targetTypeKey ->
                when (targetTypeKey) {
                    0 -> {
                        val cmValue = (selectedType as? SmartStepHeightType.CM)?.selectedValue ?: 175
                        VerticalNumberPicker(
                            range = 100..250,
                            selectedValue = cmValue,
                            onValueChange = {
                                onChange(SmartStepHeightType.CM(it))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    1 -> {
                        val ftValue = (selectedType as? SmartStepHeightType.FtInch)?.selectedFt ?: 5
                        val inValue = (selectedType as? SmartStepHeightType.FtInch)?.selectedInch ?: 9
                        Row(modifier = Modifier.fillMaxWidth()) {
                            key("ft") {
                                VerticalNumberPicker(
                                    range = 1..8,
                                    selectedValue = ftValue,
                                    onValueChange = {
                                        onChange(SmartStepHeightType.FtInch(it, inValue))
                                    },
                                    modifier = Modifier.weight(1f),
                                    unit = stringResource(R.string.ft)
                                )
                            }
                            key("in") {
                                VerticalNumberPicker(
                                    range = 0..11,
                                    selectedValue = inValue,
                                    onValueChange = {
                                        onChange(SmartStepHeightType.FtInch(ftValue, it))
                                    },
                                    modifier = Modifier.weight(1f),
                                    unit = stringResource(R.string.in_unit)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

sealed class SmartStepWeightType(
    val title: UiText
) {
    data class KG(val selectedKg: Int = 65): SmartStepWeightType(UiText.StringResource(R.string.kg))
    data class LBS(val selectedLbs: Int = 143): SmartStepWeightType(UiText.StringResource(R.string.lbs))
}

@Composable
fun SmartStepWeightPicker(
    selectedType: SmartStepWeightType,
    onChange: (type: SmartStepWeightType) -> Unit,
    modifier: Modifier = Modifier
) {

    SmartStepPickerCard(
        title = stringResource(R.string.weight),
        subtitle = stringResource(R.string.used_to_calculate_calories),
        modifier = modifier,
        unitSelector = {
            Row(modifier = Modifier.fillMaxWidth()) {
                SmartStepChip(
                    text = stringResource(R.string.kg),
                    isSelected = selectedType is SmartStepWeightType.KG,
                    style = SmartStepChipStyle.LEFT,
                    onClick = {
                        onChange(SmartStepWeightType.KG())
                    },
                    modifier = Modifier.weight(1f)
                )
                SmartStepChip(
                    text = stringResource(R.string.lbs),
                    isSelected = selectedType is SmartStepWeightType.LBS,
                    style = SmartStepChipStyle.RIGHT,
                    onClick = {
                        onChange(SmartStepWeightType.LBS())
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        },
        pickerContent = {
            val typeKey = when (selectedType) {
                is SmartStepWeightType.KG -> 0
                is SmartStepWeightType.LBS -> 1
            }

            AnimatedContent(
                targetState = typeKey,
                transitionSpec = {
                    (fadeIn() + scaleIn()).togetherWith(fadeOut() + scaleOut())
                },
                label = "WeightPickerAnimation"
            ) { targetTypeKey ->
                when (targetTypeKey) {
                    0 -> {
                        val kgValue = (selectedType as? SmartStepWeightType.KG)?.selectedKg ?: 65
                        VerticalNumberPicker(
                            range = 30..200,
                            selectedValue = kgValue,
                            unit = stringResource(R.string.kg),
                            onValueChange = {
                                onChange(SmartStepWeightType.KG(it))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    1 -> {
                        val lbsValue = (selectedType as? SmartStepWeightType.LBS)?.selectedLbs ?: 143
                        VerticalNumberPicker(
                            range = 70..450,
                            selectedValue = lbsValue,
                            unit = stringResource(R.string.lbs),
                            onValueChange = {
                                onChange(SmartStepWeightType.LBS(it))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun HeightPickerCmPreview() {
    SmartStepTheme {
        var selectedHeightType: SmartStepHeightType by remember {
            mutableStateOf(SmartStepHeightType.CM())
        }
        Box(modifier = Modifier.padding(16.dp)) {
            SmartStepHeightPicker(
                selectedType = selectedHeightType,
                onChange = {
                    selectedHeightType = it
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeightPickerPreview() {
    SmartStepTheme {
        var selectedWeightType: SmartStepWeightType by remember {
            mutableStateOf(SmartStepWeightType.KG())
        }
        Box(modifier = Modifier.padding(16.dp)) {
            SmartStepWeightPicker(
                selectedType = selectedWeightType,
                onChange = {
                    selectedWeightType = it
                },
            )
        }
    }
}

@Preview(showBackground = true, device = DESKTOP)
@Composable
private fun PickerComparisonPreview() {
    SmartStepTheme {
        var selectedHeightType: SmartStepHeightType by remember {
            mutableStateOf(SmartStepHeightType.CM())
        }
        var selectedWeightType: SmartStepWeightType by remember {
            mutableStateOf(SmartStepWeightType.KG())
        }
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SmartStepHeightPicker(
                selectedType = selectedHeightType,
                onChange = {
                    selectedHeightType = it
                },
                modifier = Modifier
                    .widthIn(
                        max = 328.dp
                    )
            )
            SmartStepWeightPicker(
                selectedType = selectedWeightType,
                onChange = {
                    selectedWeightType = it
                },
                modifier = Modifier
                    .widthIn(
                        max = 328.dp
                    )
            )
        }
    }
}
