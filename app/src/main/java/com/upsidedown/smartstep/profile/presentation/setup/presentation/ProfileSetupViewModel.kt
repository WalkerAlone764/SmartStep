package com.upsidedown.smartstep.profile.presentation.setup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightType
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepWeightType
import com.upsidedown.smartstep.core.presentation.util.Gender
import com.upsidedown.smartstep.profile.domain.HeightUnit
import com.upsidedown.smartstep.profile.domain.Profile
import com.upsidedown.smartstep.profile.domain.ProfileDataSource
import com.upsidedown.smartstep.profile.domain.WeightUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProfileSetupViewModel(
    private val profileDataSource: ProfileDataSource
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProfileSetupState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                viewModelScope.launch {
                    profileDataSource.profile.first()?.let { savedProfile ->
                        _state.update { it.copy(
                            selectedGender = savedProfile.gender,
                            selectedHeightType = when (savedProfile.heightUnit) {
                                HeightUnit.CM -> SmartStepHeightType.CM(selectedValue = savedProfile.heightCm)
                                HeightUnit.FT_IN -> {
                                    val totalInches = (savedProfile.heightCm / 2.54).roundToInt()
                                    SmartStepHeightType.FtInch(
                                        selectedFt = totalInches / 12,
                                        selectedInch = totalInches % 12
                                    )
                                }
                            },
                            selectedWeightType = when (savedProfile.weightUnit) {
                                WeightUnit.KG -> SmartStepWeightType.KG(selectedKg = savedProfile.weightKg)
                                WeightUnit.LBS -> SmartStepWeightType.LBS(
                                    selectedLbs = (savedProfile.weightKg / 0.453592).roundToInt()
                                )
                            }
                        ) }
                    }
                }
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
            ProfileSetupAction.OnStartClick -> onStartClick()
            ProfileSetupAction.OnSkip -> onSkip()
        }
    }

    private fun onSkip() {
        viewModelScope.launch {
            profileDataSource.saveIsSetupVisited(true)
        }
    }

    private fun onStartClick() {
        val currentState = _state.value
        val heightCm = when (val height = currentState.selectedHeightType) {
            is SmartStepHeightType.CM -> height.selectedValue
            is SmartStepHeightType.FtInch -> {
                val totalInches = height.selectedFt * 12 + height.selectedInch
                (totalInches * 2.54).roundToInt()
            }
        }

        val weightKg = when (val weight = currentState.selectedWeightType) {
            is SmartStepWeightType.KG -> weight.selectedKg
            is SmartStepWeightType.LBS -> (weight.selectedLbs * 0.453592).roundToInt()
        }

        val heightUnit = when (currentState.selectedHeightType) {
            is SmartStepHeightType.CM -> HeightUnit.CM
            is SmartStepHeightType.FtInch -> HeightUnit.FT_IN
        }
        val weightUnit = when (currentState.selectedWeightType) {
            is SmartStepWeightType.KG -> WeightUnit.KG
            is SmartStepWeightType.LBS -> WeightUnit.LBS
        }

        val profile = Profile(
            gender = currentState.selectedGender,
            heightCm = heightCm,
            weightKg = weightKg,
            heightUnit = heightUnit,
            weightUnit = weightUnit
        )

        viewModelScope.launch {
            profileDataSource.saveProfile(profile)
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
