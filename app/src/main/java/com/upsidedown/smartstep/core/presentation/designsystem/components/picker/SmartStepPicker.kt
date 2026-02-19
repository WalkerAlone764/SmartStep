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
import androidx.compose.runtime.retain.retain
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
import java.time.LocalDate
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun SmartStepPickerCard(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    okText: String = stringResource(R.string.save),
    cancelText: String = stringResource(R.string.cancel),
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
            .fillMaxWidth(),
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
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            unitSelector()

            Spacer(modifier = Modifier.height(if (subtitle == null) 8.dp else 24.dp))

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
                    text = cancelText,
                    onClick = onCancel,
                    style = SmartStepButtonStyle.TEXT
                )
                SmartStepButton(
                    text = okText,
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
    unit: String? = null,
    label: (Int) -> String = { it.toString() }
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
                            text = label(value),
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
    data class CM(val selectedValue: Int = 175, val unit: UiText = UiText.StringResource(R.string.cm)): SmartStepHeightType(UiText.StringResource(R.string.cm))
    data class FtInch(val selectedFt: Int = 5, val unitFt: UiText = UiText.StringResource(R.string.ft), val selectedInch: Int = 9, val unitInch: UiText = UiText.StringResource(R.string.inch)): SmartStepHeightType(UiText.StringResource(R.string.ft_in))
}

@Composable
fun SmartStepHeightPicker(
    selectedType: SmartStepHeightType,
    onDismiss: () -> Unit,
    onConfirm: (type: SmartStepHeightType) -> Unit,
    modifier: Modifier = Modifier,
) {
    var internalState by retain(selectedType) { mutableStateOf(selectedType) }

    SmartStepPickerCard(
        title = stringResource(R.string.height),
        subtitle = stringResource(R.string.used_to_calculate_distance),
        modifier = modifier,
        onCancel = onDismiss,
        onOk = {
            onConfirm(internalState)
            onDismiss()
        },
        unitSelector = {
            Row(modifier = Modifier.fillMaxWidth()) {
                SmartStepChip(
                    text = stringResource(R.string.cm),
                    isSelected = internalState is SmartStepHeightType.CM,
                    style = SmartStepChipStyle.LEFT,
                    onClick = {
                        val current = internalState
                        if (current is SmartStepHeightType.FtInch) {
                            val totalInches = current.selectedFt * 12 + current.selectedInch
                            val cm = (totalInches * 2.54).roundToInt()
                            internalState = SmartStepHeightType.CM(selectedValue = cm)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                SmartStepChip(
                    text = stringResource(R.string.ft_in),
                    isSelected = internalState is SmartStepHeightType.FtInch,
                    style = SmartStepChipStyle.RIGHT,
                    onClick = {
                        val current = internalState
                        if (current is SmartStepHeightType.CM) {
                            val totalInches = (current.selectedValue / 2.54).roundToInt()
                            internalState = SmartStepHeightType.FtInch(
                                selectedFt = totalInches / 12,
                                selectedInch = totalInches % 12
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        },
        pickerContent = {
            val typeKey = when (internalState) {
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
                        val cmValue = (internalState as? SmartStepHeightType.CM)?.selectedValue ?: 175
                        VerticalNumberPicker(
                            range = 100..250,
                            selectedValue = cmValue,
                            onValueChange = {
                                internalState = SmartStepHeightType.CM(it)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    1 -> {
                        val ftValue = (internalState as? SmartStepHeightType.FtInch)?.selectedFt ?: 5
                        val inValue = (internalState as? SmartStepHeightType.FtInch)?.selectedInch ?: 9
                        Row(modifier = Modifier.fillMaxWidth()) {
                            key("ft") {
                                VerticalNumberPicker(
                                    range = 1..8,
                                    selectedValue = ftValue,
                                    onValueChange = {
                                        internalState = SmartStepHeightType.FtInch(it, selectedInch = inValue)
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
                                        internalState = SmartStepHeightType.FtInch(ftValue, selectedInch = it)
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
    data class KG(val selectedKg: Int = 65, val unit: UiText = UiText.StringResource(R.string.kg)): SmartStepWeightType(UiText.StringResource(R.string.kg))
    data class LBS(val selectedLbs: Int = 143, val unit: UiText = UiText.StringResource(R.string.lbs)): SmartStepWeightType(UiText.StringResource(R.string.lbs))
}

@Composable
fun SmartStepWeightPicker(
    selectedType: SmartStepWeightType,
    onDismiss: () -> Unit,
    onConfirm: (type: SmartStepWeightType) -> Unit,
    modifier: Modifier = Modifier
) {
    var internalState by retain(selectedType) { mutableStateOf(selectedType) }

    SmartStepPickerCard(
        title = stringResource(R.string.weight),
        subtitle = stringResource(R.string.used_to_calculate_calories),
        modifier = modifier,
        onCancel = onDismiss,
        onOk = {
            onConfirm(internalState)
            onDismiss()
        },
        unitSelector = {
            Row(modifier = Modifier.fillMaxWidth()) {
                SmartStepChip(
                    text = stringResource(R.string.kg),
                    isSelected = internalState is SmartStepWeightType.KG,
                    style = SmartStepChipStyle.LEFT,
                    onClick = {
                        val current = internalState
                        if (current is SmartStepWeightType.LBS) {
                            val kg = (current.selectedLbs * 0.453592).roundToInt()
                            internalState = SmartStepWeightType.KG(selectedKg = kg)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                SmartStepChip(
                    text = stringResource(R.string.lbs),
                    isSelected = internalState is SmartStepWeightType.LBS,
                    style = SmartStepChipStyle.RIGHT,
                    onClick = {
                        val current = internalState
                        if (current is SmartStepWeightType.KG) {
                            val lbs = (current.selectedKg / 0.453592).roundToInt()
                            internalState = SmartStepWeightType.LBS(selectedLbs = lbs)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        },
        pickerContent = {
            val typeKey = when (internalState) {
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
                        val kgValue = (internalState as? SmartStepWeightType.KG)?.selectedKg ?: 65
                        VerticalNumberPicker(
                            range = 30..200,
                            selectedValue = kgValue,
                            unit = stringResource(R.string.kg),
                            onValueChange = {
                                internalState = SmartStepWeightType.KG(it)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    1 -> {
                        val lbsValue = (internalState as? SmartStepWeightType.LBS)?.selectedLbs ?: 143
                        VerticalNumberPicker(
                            range = 70..450,
                            selectedValue = lbsValue,
                            unit = stringResource(R.string.lbs),
                            onValueChange = {
                                internalState = SmartStepWeightType.LBS(it)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun SmartStepDatePicker(
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.date), // Should be stringResource(R.string.date) if exists
) {
    var selectedDate by retain(initialDate) { mutableStateOf(initialDate) }

    SmartStepPickerCard(
        title = title,
        modifier = modifier,
        onCancel = onDismiss,
        onOk = {
            onConfirm(selectedDate)
            onDismiss()
        },
        pickerContent = {
            Row(modifier = Modifier.fillMaxWidth()) {
                key("year") {
                    VerticalNumberPicker(
                        range = 1900..2100,
                        selectedValue = selectedDate.year,
                        onValueChange = {
                            selectedDate = selectedDate.withYear(it)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                key("month") {
                    VerticalNumberPicker(
                        range = 1..12,
                        selectedValue = selectedDate.monthValue,
                        onValueChange = {
                            selectedDate = try {
                                selectedDate.withMonth(it)
                            } catch (e: Exception) {
                                selectedDate.withDayOfMonth(1).withMonth(it)
                            }
                        },
                        label = { it.toString().padStart(2, '0') },
                        modifier = Modifier.weight(1f)
                    )
                }
                key("day") {
                    val daysInMonth = selectedDate.lengthOfMonth()
                    VerticalNumberPicker(
                        range = 1..daysInMonth,
                        selectedValue = selectedDate.dayOfMonth.coerceAtMost(daysInMonth),
                        onValueChange = {
                            selectedDate = selectedDate.withDayOfMonth(it)
                        },
                        label = { it.toString().padStart(2, '0') },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DatePickerPreview() {
    SmartStepTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SmartStepDatePicker(
                initialDate = LocalDate.of(2025, 11, 30),
                onDismiss = {},
                onConfirm = {}
            )
        }
    }
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
                onDismiss = {},
                onConfirm = {
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
                onDismiss = {},
                onConfirm = {
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
                onDismiss = {},
                onConfirm = {
                    selectedHeightType = it
                },
                modifier = Modifier
                    .widthIn(
                        max = 328.dp
                    )
            )
            SmartStepWeightPicker(
                selectedType = selectedWeightType,
                onDismiss = {},
                onConfirm = {
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
