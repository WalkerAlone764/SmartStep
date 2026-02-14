package com.upsidedown.smartstep.profile.presentation.setting.presentation

import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightType
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepWeightType
import com.upsidedown.smartstep.core.presentation.util.Gender

data class PersonalSettingState(
    val selectedGender: Gender = Gender.FEMALE,
    val selectedHeightType: SmartStepHeightType = SmartStepHeightType.CM(),
    val selectedWeightType: SmartStepWeightType = SmartStepWeightType.KG(),
    val isGenderMenuShown: Boolean = false,
    val isHeightMenuShown: Boolean = false,
    val isWeightMenuShown: Boolean = false,
)