package com.upsidedown.smartstep.profile.presentation.setting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepHeightType
import com.upsidedown.smartstep.core.presentation.designsystem.components.picker.SmartStepWeightType
import com.upsidedown.smartstep.profile.domain.HeightUnit
import com.upsidedown.smartstep.profile.domain.Profile
import com.upsidedown.smartstep.profile.domain.ProfileDataSource
import com.upsidedown.smartstep.profile.domain.WeightUnit
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class PersonalSettingViewModel(
    private val profileDataSource: ProfileDataSource
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(PersonalSettingState())
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
            initialValue = PersonalSettingState()
        )

    private val _event = Channel<PersonalSettingEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: PersonalSettingAction) {
        when (action) {
            is PersonalSettingAction.OnGenderSelected -> {
                _state.update { it.copy(selectedGender = action.gender) }
                saveProfile()
            }
            is PersonalSettingAction.OnHeightSelected -> {
                _state.update { it.copy(
                    selectedHeightType = action.heightType,
                    isHeightMenuShown = false
                ) }
                saveProfile()
            }
            is PersonalSettingAction.OnWeightSelected -> {
                _state.update { it.copy(
                    selectedWeightType = action.weightType,
                    isWeightMenuShown = false
                ) }
                saveProfile()
            }
            is PersonalSettingAction.OnShowGenderMenu -> {
                _state.update { it.copy(isGenderMenuShown = action.isShown) }
            }
            is PersonalSettingAction.OnShowHeightMenu -> {
                _state.update { it.copy(isHeightMenuShown = action.isShown) }
            }
            is PersonalSettingAction.OnShowWeightMenu -> {
                _state.update { it.copy(isWeightMenuShown = action.isShown) }
            }

            PersonalSettingAction.OnClickSave -> onClickSave()
        }
    }

    private fun onClickSave() {
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
            _event.send(PersonalSettingEvent.OnSuccessfullySave)
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            val currentState = _state.value
            val (heightCm, heightUnit) = when (val type = currentState.selectedHeightType) {
                is SmartStepHeightType.CM -> type.selectedValue to HeightUnit.CM
                is SmartStepHeightType.FtInch -> {
                    val totalInches = type.selectedFt * 12 + type.selectedInch
                    (totalInches * 2.54).roundToInt() to HeightUnit.FT_IN
                }
            }

            val (weightKg, weightUnit) = when (val type = currentState.selectedWeightType) {
                is SmartStepWeightType.KG -> type.selectedKg to WeightUnit.KG
                is SmartStepWeightType.LBS -> (type.selectedLbs * 0.453592).roundToInt() to WeightUnit.LBS
            }

            profileDataSource.profile.first()?.let { profile ->
                profileDataSource.saveProfile(
                    profile.copy(
                        gender = currentState.selectedGender,
                        heightCm = heightCm,
                        heightUnit = heightUnit,
                        weightKg = weightKg,
                        weightUnit = weightUnit
                    )
                )
            }
        }
    }

}
