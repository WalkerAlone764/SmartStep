package com.upsidedown.smartstep.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsidedown.smartstep.core.presentation.designsystem.components.field.SmartStepDropdownField
import com.upsidedown.smartstep.core.presentation.designsystem.theme.SmartStepTheme


@Composable
fun <T> SmartStepDropdown(
    label: String,
    selectedValue: T,
    options: List<T>,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    optionToString: (T) -> String = { it.toString() }
) {
    var expanded by remember { mutableStateOf(false) }
    var itemWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Column(modifier = modifier) {
        SmartStepDropdownField(
            label = label,
            selectedValue = optionToString(selectedValue),
            onClick = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { size ->
                    itemWidth = with(density) { size.width.toDp() }
                }
        )

        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp)),
            colorScheme = MaterialTheme.colorScheme.copy(surface = MaterialTheme.colorScheme.surface)
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(itemWidth)
                    .background(Color.White)
            ) {
                options.forEach { option ->
                    val isSelected = option == selectedValue
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = optionToString(option),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.surface
                                else Color.Transparent
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SmartStepDropdownPreview() {
    SmartStepTheme {
        var selectedGender by remember { mutableStateOf("Female") }
        Box(modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp)) {
            SmartStepDropdown(
                label = "Gender",
                selectedValue = selectedGender,
                options = listOf("Female", "Male", "Other"),
                onOptionSelected = { selectedGender = it }
            )
        }
    }
}
