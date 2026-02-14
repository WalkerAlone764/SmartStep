package com.upsidedown.smartstep.profile.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.upsidedown.smartstep.core.presentation.designsystem.components.field.SmartStepDropdownField
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightPicker
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightType

@Composable
fun HeightOptionComponent(
    isHeightMenuShown: Boolean,
    selectedHeightType: SmartStepHeightType,
    heightTypeAsString: String,
    onHeightTypeSelected: (SmartStepHeightType) -> Unit,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    SmartStepDropdownField(
        label = "Height",
        selectedValue = heightTypeAsString,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
    )

    if (isHeightMenuShown) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            SmartStepHeightPicker(
                selectedType = selectedHeightType,
                onDismiss = onDismiss,
                onConfirm = {
                    onHeightTypeSelected(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
