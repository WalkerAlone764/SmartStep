package com.upsidedown.smartstep.profile.presentation.setup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightType
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepWeightType
import com.upsidedown.smartstep.core.presentation.util.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProfileSetupViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProfileSetupState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileSetupState()
        )

    fun onAction(action: ProfileSetupAction) {
        when (action) {
            is ProfileSetupAction.OnChangeSelectedGender -> onChangeSelectedGender(action.gender)
            ProfileSetupAction.OnClickHeightType -> onChangeSelectedHeightType()
            is ProfileSetupAction.OnChangeHeightType -> onChangeHeightType(action.heightType)
            ProfileSetupAction.OnDismissHeightTypeMenu -> onDismissHeightTypeMenu()
            is ProfileSetupAction.OnChangeWeightType -> onChangeWeightType(action.weightType)
            ProfileSetupAction.OnClickWeightType -> onClickWeightType()
            ProfileSetupAction.OnDismissWeightTypeMenu -> onDismissWeightTypeMenu()
        }
    }

    private fun onChangeWeightType(weightType: SmartStepWeightType) {
        _state.update { it.copy(
            selectedWeightType = weightType
        ) }
    }

    private fun onDismissWeightTypeMenu() {
        _state.update { it.copy(
            isShownWeightMenu = false
        ) }
    }

    private fun onClickWeightType() {
        _state.update { it.copy(
            isShownWeightMenu = true
        ) }
    }

    private fun onDismissHeightTypeMenu() {
        _state.update { it.copy(
            isShowHeightMenu = false
        ) }
    }

    private fun onChangeHeightType(heightType: SmartStepHeightType) {
        _state.update { it.copy(
            selectedHeightType = heightType
        ) }
    }

    private fun onChangeSelectedHeightType() {
        _state.update { it.copy(
            isShowHeightMenu = true
        ) }
    }

    private fun onChangeSelectedGender(gender: Gender) {
        _state.update { it.copy(
            selectedGender = gender
        ) }
    }

}