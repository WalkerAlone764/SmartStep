package com.upsidedown.smartstep.home.presentation.step_counter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StepCounterViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(StepCounterState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }

//            _event.send(StepCounterEvent.CheckAllPermission)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = StepCounterState()
        )

    private val _event = Channel<StepCounterEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: StepCounterAction) {
        when (action) {
            StepCounterAction.OnClickExit -> {
                _state.update { it.copy(isExitDialogShown = true) }
            }

            StepCounterAction.OnClickOk -> onClickOk()
            StepCounterAction.OnDismissCloseDialog -> onDismissCloseDialog()
            is StepCounterAction.OnCheckPermissionResult -> onCheckPermissionResult( action.activityRecognitionResult, action.backgroundLocationResult)
            StepCounterAction.OnClickAllowPhysicalActivity -> onClickAllowPhysicalActivity()
            StepCounterAction.OnClickOpenSettings -> onClickOpenSetting()
            is StepCounterAction.OnResultRequestingPhysicalActivity -> onResultRequestingPhysicalActivity(action.result)
            is StepCounterAction.OnCheckCanShowRationaleForPhysicalActivity -> onCheckCanShowRationaleForPhysicalActivity(action.canShowRationale)
            StepCounterAction.OnClickAllowIgnoreBatteryOptimization -> onClickAllowIgnoreBatteryOptimization()
            StepCounterAction.OnDismissIgnoreBatteryOptimizationDialog -> onDismissIgnoreBatteryOptimizationDialog()
            StepCounterAction.OnResume -> onResume()
            StepCounterAction.OnClickFixStopCountingStep -> onClickFixStopCountingStep()
            StepCounterAction.OnClickStepGoalMenu -> onClickStepGoalMenu()
            StepCounterAction.OnDismissStepGoalSelectionDialog -> onDismissStepGoalSelectionDialog()
            is StepCounterAction.OnSaveStepGoal -> onSaveStepGoal(action.stepGoal)
        }
    }

    private fun onSaveStepGoal(stepGoal: Int) {
        _state.update { it.copy(
            goalSteps = stepGoal,
            isStepGoalSelectionDialogShown = false,
        ) }
    }

    private fun onDismissStepGoalSelectionDialog() {
        _state.update { it.copy(isStepGoalSelectionDialogShown = false) }
    }

    private fun onClickStepGoalMenu() {
        _state.update { it.copy(isStepGoalSelectionDialogShown = true) }
    }

    private fun onClickFixStopCountingStep() {
        _state.update { it.copy(isIgnoreBatteryOptimizationDialogShown = true) }
    }

    private fun onResume() {
       viewModelScope.launch {
           _event.send(StepCounterEvent.CheckAllPermission)
       }
    }

    private fun onClickAllowIgnoreBatteryOptimization() {
        _state.update { it.copy(isIgnoreBatteryOptimizationDialogShown = false) }
        _event.trySend(StepCounterEvent.RequestForBatteryOptimization)
    }

    private fun onDismissIgnoreBatteryOptimizationDialog() {
        _state.update { it.copy(isIgnoreBatteryOptimizationDialogShown = false) }
    }

    private fun onClickOpenSetting() {
        _state.update { it.copy(
            isActivityRecognitionPermissionSettingDialogShown = false,
            isActivityRecognitionPermissionRationaleDialogShown = false
        ) }

        _event.trySend(StepCounterEvent.OpenSetting)
    }

    private fun onClickAllowPhysicalActivity() {
        _state.update { it.copy(
            isActivityRecognitionPermissionSettingDialogShown = false,
            isActivityRecognitionPermissionRationaleDialogShown = false
        ) }
        _event.trySend(StepCounterEvent.RequestPhysicalActivityPermission)
    }

    private fun onCheckCanShowRationaleForPhysicalActivity(canShowRationale: Boolean) {
            _state.update { it.copy(
                isActivityRecognitionPermissionRationaleDialogShown = canShowRationale,
                isActivityRecognitionPermissionSettingDialogShown = !canShowRationale,
            ) }

    }

    private fun onResultRequestingPhysicalActivity(result: Boolean) {
        _state.update {
            it.copy(
                hasActivityRecognitionPermission = result,
            )
        }

        if (!result) {
            _event.trySend(StepCounterEvent.CheckCanShowRequestPhysicalActivityPermission)
            return
        }

        if (!_state.value.hasIgnoreBatteryOptimizationPermission) {
            _state.update { it.copy(
                isIgnoreBatteryOptimizationDialogShown = true
            ) }
        }

    }

    private fun onCheckPermissionResult(
        activityRecognitionResult: Boolean,
        ignoreBatteryOptimizationIgnore: Boolean
    ) {
        _state.update {
            it.copy(
                hasActivityRecognitionPermission = activityRecognitionResult,
                hasIgnoreBatteryOptimizationPermission = ignoreBatteryOptimizationIgnore,
                isActivityRecognitionPermissionRationaleDialogShown = !activityRecognitionResult
            )
        }


    }


    private fun onClickOk() {
        _event.trySend(StepCounterEvent.ExitApp)
    }

    private fun onDismissCloseDialog() {
        _state.update { it.copy(isExitDialogShown = false) }
    }

}