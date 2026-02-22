package com.upsidedown.smartstep.home.presentation.step_counter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsidedown.smartstep.home.domain.StepDataSource
import com.upsidedown.smartstep.home.domain.StepRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.roundToInt

class StepCounterViewModel(
    private val stepRepository: StepRepository,
    private val stepDataSource: StepDataSource
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(StepCounterState())
    private val _steps = stepRepository
        .getAllStepsByDate(LocalDate.now())


    val state = _state
        .combine(_steps) { state, steps ->
            val totalSteps = steps.sumOf { it.count }
            // Simple calculation for demonstration
            val distance = (totalSteps * 0.00075 * 10).roundToInt() / 10.0
            val calories = (totalSteps * 0.04).roundToInt()
            val time = (totalSteps * 0.01).roundToInt()

            state.copy(
                currentSteps = totalSteps,
                distanceKm = distance,
                calories = calories,
                timeMin = time
            )
        }
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                _event.send(StepCounterEvent.RequestPhysicalActivityPermission)
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = StepCounterState()
        )

    init {

        viewModelScope.launch {
            stepDataSource.listenSteps()
        }
    }

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
            StepCounterAction.OnClickPersonalSettingMenu -> Unit
            StepCounterAction.OnClickPauseResume -> {
                _state.update { it.copy(isPaused = !it.isPaused) }
                if (_state.value.isPaused) {
                    _event.trySend(StepCounterEvent.StopStepCounterService)
                } else {
                    _event.trySend(StepCounterEvent.StartStepCounterService)
                }
            }
            StepCounterAction.OnClickEditSteps -> {
                _state.update { it.copy(isEditStepsDialogShown = true) }
            }
            StepCounterAction.OnDismissEditStepsDialog -> {
                _state.update { it.updateEditStepsDialogShown(false) }
            }
            is StepCounterAction.OnSaveEditSteps -> onSaveEditSteps()
            StepCounterAction.OnClickDate -> {
                _state.update { it.copy(isDatePickerDialogShown = true) }
            }
            StepCounterAction.OnDismissDatePickerDialog -> {
                _state.update { it.copy(isDatePickerDialogShown = false) }
            }
            is StepCounterAction.OnDateSelected -> {
                _state.update { it.copy(selectedDateInEdit = action.date, isDatePickerDialogShown = false) }
            }
        }
    }

    private fun onSaveEditSteps() {
        viewModelScope.launch {
            val currentState = _state.value
            val steps = currentState.stepsInEdit.text.toString()
            Log.d("Save Steps", steps)
            if (steps.toIntOrNull() == null) {
                return@launch
            }
            stepRepository.updateStepsByDate(currentState.selectedDateInEdit,steps.toInt() )
            _state.update { it.updateEditStepsDialogShown(false) }
        }
    }

    private fun StepCounterState.updateEditStepsDialogShown(shown: Boolean): StepCounterState {
        return copy(isEditStepsDialogShown = shown)
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

        if (result) {
            if (!_state.value.hasIgnoreBatteryOptimizationPermission) {
                _state.update { it.copy(
                    isIgnoreBatteryOptimizationDialogShown = true
                ) }
            }
        }

    }

    private fun onCheckPermissionResult(
        activityRecognitionResult: Boolean,
        ignoreBatteryOptimizationIgnore: Boolean
    ) {
        viewModelScope.launch {
            val prevState = _state.value
            _state.update {
                it.copy(
                    hasActivityRecognitionPermission = activityRecognitionResult,
                    hasIgnoreBatteryOptimizationPermission = ignoreBatteryOptimizationIgnore,
                )
            }

            // If activity recognition was just granted (e.g. user came back from settings)
            // and battery optimization is not yet ignored, show the dialog.
            if (!prevState.hasActivityRecognitionPermission && activityRecognitionResult) {
                if (!ignoreBatteryOptimizationIgnore) {
                    _state.update { it.copy(isIgnoreBatteryOptimizationDialogShown = true) }
                }
            }
        }

    }


    private fun onClickOk() {
        _event.trySend(StepCounterEvent.ExitApp)
    }

    private fun onDismissCloseDialog() {
        _state.update { it.copy(isExitDialogShown = false) }
    }

}
