package com.upsidedown.smartstep.profile.presentation.setup.presentation

import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightType
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepWeightType
import com.upsidedown.smartstep.core.presentation.util.Gender

sealed interface ProfileSetupAction {

    data class OnChangeSelectedGender(val gender: Gender): ProfileSetupAction

    data object OnClickHeightType: ProfileSetupAction

    data class OnChangeHeightType(val heightType: SmartStepHeightType): ProfileSetupAction

    data object OnDismissHeightTypeMenu: ProfileSetupAction

    data object OnClickWeightType: ProfileSetupAction

    data class OnChangeWeightType(val weightType: SmartStepWeightType) : ProfileSetupAction

    data object OnDismissWeightTypeMenu: ProfileSetupAction

}