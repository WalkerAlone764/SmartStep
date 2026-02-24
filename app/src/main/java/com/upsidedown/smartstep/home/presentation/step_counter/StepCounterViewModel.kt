package com.upsidedown.smartstep.home.presentation.step_counter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsidedown.smartstep.core.database.domain.model.Step
import com.upsidedown.smartstep.core.presentation.util.Gender
import com.upsidedown.smartstep.home.domain.StepDataSource
import com.upsidedown.smartstep.home.domain.StepRepository
import com.upsidedown.smartstep.profile.domain.HeightUnit
import com.upsidedown.smartstep.profile.domain.ProfileDataSource
import com.upsidedown.smartstep.profile.domain.WeightUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.roundToInt

class StepCounterViewModel(
    private val stepRepository: StepRepository,
    private val stepDataSource: StepDataSource,
    private val profileDataSource: ProfileDataSource
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(StepCounterState())
    private val _steps = stepRepository
        .getAllStepsByDate(LocalDate.now())

    private val _pastSevenDaysSteps = stepRepository
        .getStepsInRange(LocalDate.now().minusDays(6), LocalDate.now())

    private val _profile = profileDataSource.profile

    private val lastStepCount = MutableStateFlow(0)
    val stepCounts =  _steps
        .map { it.sumOf { it.count } }
        .runningReduce {
            accumulator, value ->
            if (accumulator - lastStepCount.value >= 10) {
                lastStepCount.value = accumulator
            }
            accumulator + value
        }



    val state = combine(_state, _steps,_pastSevenDaysSteps, _profile) {
            state, steps, pastSteps, profile ->
            if (state.isPaused) {
                state // Frozen when paused
            } else {
                calculateAndUpdateMetrics(state, steps, pastSteps, profile)
            }
        }

        .flowOn(Dispatchers.IO)
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
            is StepCounterAction.OnCheckPermissionResult -> onCheckPermissionResult( 
                action.activityRecognitionResult, 
                action.backgroundLocationResult,
                action.notificationPermissionResult
            )
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
            is StepCounterAction.OnResultRequestingNotificationPermission -> {
                _state.update { it.copy(hasNotificationPermission = action.result) }
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
        _state.update {
            it.copy(
                goalSteps = stepGoal,
                isStepGoalSelectionDialogShown = false,
            )
        }
    }

    private fun onDismissStepGoalSelectionDialog() {
        _state.update {
            it.copy(isStepGoalSelectionDialogShown = false)
        }
    }

    private fun onClickStepGoalMenu() {
        _state.update {
            it.copy(isStepGoalSelectionDialogShown = true)
        }
    }

    private fun onClickFixStopCountingStep() {
        _state.update {
            it.copy(isIgnoreBatteryOptimizationDialogShown = true)
        }
    }

    private fun onResume() {
       viewModelScope.launch {
           _event.send(StepCounterEvent.CheckAllPermission)
       }
    }

    private fun onClickAllowIgnoreBatteryOptimization() {
        _state.update {
            it.copy(isIgnoreBatteryOptimizationDialogShown = false)
        }
        _event.trySend(StepCounterEvent.RequestForBatteryOptimization)
    }

    private fun onDismissIgnoreBatteryOptimizationDialog() {
        _state.update {
            it.copy(isIgnoreBatteryOptimizationDialogShown = false)
        }
    }

    private fun onClickOpenSetting() {
        _state.update {
            it.copy(
                isActivityRecognitionPermissionSettingDialogShown = false,
                isActivityRecognitionPermissionRationaleDialogShown = false
            )
        }

        _event.trySend(StepCounterEvent.OpenSetting)
    }

    private fun onClickAllowPhysicalActivity() {
        _state.update {
            it.copy(
                isActivityRecognitionPermissionSettingDialogShown = false,
                isActivityRecognitionPermissionRationaleDialogShown = false
            )
        }
        _event.trySend(StepCounterEvent.RequestPhysicalActivityPermission)
    }

    private fun onCheckCanShowRationaleForPhysicalActivity(canShowRationale: Boolean) {
            _state.update {
                it.copy(
                    isActivityRecognitionPermissionRationaleDialogShown = canShowRationale,
                    isActivityRecognitionPermissionSettingDialogShown = !canShowRationale,
                )
            }

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
                _state.update {
                    it.copy(isIgnoreBatteryOptimizationDialogShown = true)
                }
            }
        }

    }


    private fun onCheckPermissionResult(
        activityRecognitionResult: Boolean,
        ignoreBatteryOptimizationIgnore: Boolean,
        notificationPermissionResult: Boolean
    ) {
        viewModelScope.launch {
            val prevState = _state.value
            _state.update {
                it.copy(
                    hasActivityRecognitionPermission = activityRecognitionResult,
                    hasIgnoreBatteryOptimizationPermission = ignoreBatteryOptimizationIgnore,
                    hasNotificationPermission = notificationPermissionResult
                )
            }

            // If activity recognition was just granted (e.g. user came back from settings)
            // and battery optimization is not yet ignored, show the dialog.
            if (!prevState.hasActivityRecognitionPermission && activityRecognitionResult) {
                if (!ignoreBatteryOptimizationIgnore) {
                    _state.update {
                        it.copy(isIgnoreBatteryOptimizationDialogShown = true)
                    }
                } else if (!notificationPermissionResult) {
                     _event.send(StepCounterEvent.RequestNotificationPermission)
                }
            }
            
            // If battery optimization was just granted (e.g. user came back from system dialog)
            // and notification permission is not yet granted, request it.
            if (!prevState.hasIgnoreBatteryOptimizationPermission && ignoreBatteryOptimizationIgnore) {
                if (!notificationPermissionResult) {
                    _event.send(StepCounterEvent.RequestNotificationPermission)
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

    private fun calculateAndUpdateMetrics(state: StepCounterState, steps: List<Step>, pastSteps: List<Step>, profile: com.upsidedown.smartstep.profile.domain.Profile?): StepCounterState {
        val totalSteps = steps.sumOf { it.count }
        Log.d("StepCounterViewModel", "is call: ${totalSteps - lastStepCount.value >= 10}")


        // Check if step count has increased by at least 10 since the last calculation
        if (totalSteps - lastStepCount.value >= 10) {
            Log.d("StepCounterViewModel", "is calculating")
            val heightCm = profile?.height ?: 170 // Default to 170cm if profile not set
            val isMetric = profile?.heightUnit != HeightUnit.FT_IN

            val stepLengthCm = heightCm * 0.415
            val distanceMeters = totalSteps * stepLengthCm / 100

            val (distance, unit) = if (isMetric) {
                val km = distanceMeters / 1000
                (km * 10).roundToInt() / 10.0 to "km"
            } else {
                val mi = distanceMeters / 1609.34
                (mi * 10).roundToInt() / 10.0 to "mi"
            }

            // Calculate calories
            var calories = 0
            if (profile != null) {
                var weightKg = profile.weight
                // Assuming WeightUnit.LB and profile.weightLb exist
                if (profile.weightUnit == WeightUnit.LBS) {
                    weightKg = (profile.weight / 2.20462).roundToInt()
                }

                val genderFactor = when (profile.gender) { // Assuming Gender enum exists
                    Gender.MALE -> 1.0
                    Gender.FEMALE -> 0.9
                }

                val kcalPerStep = weightKg * 0.0005 * genderFactor
                calories = (totalSteps * kcalPerStep).roundToInt()
            }

            val time = (totalSteps * 0.01).roundToInt()

            val today = LocalDate.now()
            val lastSevenDays = (0..6).map { today.minusDays(it.toLong()) }.reversed()
            val pastStepsByDate = pastSteps.groupBy { it.date }
                .mapValues { it.value.sumOf { step -> step.count } }

            val pastSevenDaysStepsMap = lastSevenDays.map {
                date ->
                mapOf("date" to date, "step" to (pastStepsByDate[date] ?: 0))
            }

            // Update lastStepCount to the current totalSteps
            lastStepCount.value = totalSteps

            return state.copy(
                currentSteps = totalSteps,
                distance = distance,
                distanceUnit = unit,
                calories = calories,
                timeMin = time,
                pastSevenDaysSteps = pastSevenDaysStepsMap
            )
        } else {
            // If step count hasn't increased by 10, return the state without updating calories
            // but still update other metrics if they depend on totalSteps
            val heightCm = profile?.height ?: 170
            val isMetric = profile?.heightUnit != HeightUnit.FT_IN
            val stepLengthCm = heightCm * 0.415
            val distanceMeters = totalSteps * stepLengthCm / 100
            val (distance, unit) = if (isMetric) {
                val km = distanceMeters / 1000
                (km * 10).roundToInt() / 10.0 to "km"
            } else {
                val mi = distanceMeters / 1609.34
                (mi * 10).roundToInt() / 10.0 to "mi"
            }
            val time = (totalSteps * 0.01).roundToInt()
            val today = LocalDate.now()
            val lastSevenDays = (0..6).map { today.minusDays(it.toLong()) }.reversed()
            val pastStepsByDate = pastSteps.groupBy { it.date }
                .mapValues { it.value.sumOf { step -> step.count } }
            val pastSevenDaysStepsMap = lastSevenDays.map {
                date ->
                mapOf("date" to date, "step" to (pastStepsByDate[date] ?: 0))
            }
            return state.copy(
                currentSteps = totalSteps,
                distance = distance,
                distanceUnit = unit,
                // calories remain unchanged if step count has not increased by 10
                timeMin = time,
                pastSevenDaysSteps = pastSevenDaysStepsMap
            )
        }
    }
}
