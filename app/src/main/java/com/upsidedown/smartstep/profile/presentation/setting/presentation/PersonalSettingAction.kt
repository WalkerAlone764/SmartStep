package com.upsidedown.smartstep.profile.presentation.setting.presentation

import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightType
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepWeightType
import com.upsidedown.smartstep.core.presentation.util.Gender

sealed interface PersonalSettingAction {
    data class OnGenderSelected(val gender: Gender) : PersonalSettingAction
    data class OnHeightSelected(val heightType: SmartStepHeightType) : PersonalSettingAction
    data class OnWeightSelected(val weightType: SmartStepWeightType) : PersonalSettingAction
    data class OnShowGenderMenu(val isShown: Boolean) : PersonalSettingAction
    data class OnShowHeightMenu(val isShown: Boolean) : PersonalSettingAction
    data class OnShowWeightMenu(val isShown: Boolean) : PersonalSettingAction

    data object OnClickSave: PersonalSettingAction
}