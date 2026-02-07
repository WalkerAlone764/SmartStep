package com.upsidedown.smartstep.profile.presentation.setup.presentation

import android.content.Context
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightType
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepWeightType
import com.upsidedown.smartstep.core.presentation.util.Gender

data class ProfileSetupState(
    val selectedGender: Gender = Gender.FEMALE,
    val selectedHeightType: SmartStepHeightType = SmartStepHeightType.CM(),
    val selectedWeightType: SmartStepWeightType = SmartStepWeightType.KG(),
    val isShowHeightMenu: Boolean = false,
    val isShownWeightMenu: Boolean = false
) {
    fun selectedHeightAsString(context: Context): String {

        return when (selectedHeightType) {
            is SmartStepHeightType.CM -> "${selectedHeightType.selectedValue} ${
                selectedHeightType.unit.asString(
                    context
                )
            }"

            is SmartStepHeightType.FtInch -> "${selectedHeightType.selectedFt} ${
                selectedHeightType.unitFt.asString(
                    context
                )
            }-${selectedHeightType.selectedInch} ${selectedHeightType.unitInch.asString(context)}"
        }
    }

    fun selectedWeightAsString(context: Context): String {
        return when(selectedWeightType) {
            is SmartStepWeightType.KG -> "${selectedWeightType.selectedKg} ${selectedWeightType.unit.asString(context)}"
            is SmartStepWeightType.LBS -> "${selectedWeightType.selectedLbs} ${selectedWeightType.unit.asString(context)}"
        }
    }
}