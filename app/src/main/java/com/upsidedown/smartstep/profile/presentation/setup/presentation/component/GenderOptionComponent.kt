package com.upsidedown.smartstep.profile.presentation.setup.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.upsidedown.smartstep.core.presentation.designsystem.components.SmartStepDropdown
import com.upsidedown.smartstep.core.presentation.util.Gender

@Composable
fun GenderOptionComponent(
    selectedGender: Gender,
    onGenderSelected: (Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    SmartStepDropdown(
        label = "Gender",
        selectedValue = selectedGender,
        onOptionSelected = {
            onGenderSelected(it)
        },
        options = Gender.entries,
        optionToString = { it.uiText.asString(context) },
        modifier = modifier
    )
}