package com.upsidedown.smartstep.profile.presentation.setup.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.upsidedown.smartstep.core.presentation.designsystem.components.field.SmartStepDropdownField
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepWeightPicker
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepWeightType

@Composable
fun WeightOptionComponent(
    isWeightMenuShown: Boolean,
    selectedWeightType: SmartStepWeightType,
    weightTypeAsString: String,
    onWeightTypeSelected: (SmartStepWeightType) -> Unit,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    SmartStepDropdownField(
        label = "Weight",
        selectedValue = weightTypeAsString,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
    )

    if (isWeightMenuShown) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            SmartStepWeightPicker(
                selectedType = selectedWeightType,
                onDismiss = onDismiss,
                onConfirm = {
                    onWeightTypeSelected(it)
                },
                modifier = Modifier
            )
        }
    }
}