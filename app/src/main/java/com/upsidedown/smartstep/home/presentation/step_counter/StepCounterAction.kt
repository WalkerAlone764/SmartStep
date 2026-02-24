package com.upsidedown.smartstep.home.presentation.step_counter

import java.time.LocalDate

sealed interface StepCounterAction {

    data object OnResume: StepCounterAction
    data object OnClickExit: StepCounterAction

    data object OnClickOk: StepCounterAction
    data object OnDismissCloseDialog: StepCounterAction

    data class OnCheckPermissionResult(
        val activityRecognitionResult: Boolean,
        val backgroundLocationResult: Boolean,
        val notificationPermissionResult: Boolean
    ): StepCounterAction

    data object OnClickAllowPhysicalActivity: StepCounterAction

    data class OnResultRequestingPhysicalActivity(val result: Boolean): StepCounterAction

    data class OnCheckCanShowRationaleForPhysicalActivity(val canShowRationale: Boolean) : StepCounterAction

    data object OnClickOpenSettings: StepCounterAction

    data object OnDismissIgnoreBatteryOptimizationDialog: StepCounterAction
    data object OnClickAllowIgnoreBatteryOptimization: StepCounterAction

    data object OnClickFixStopCountingStep : StepCounterAction

    data object OnClickStepGoalMenu: StepCounterAction

    data object OnDismissStepGoalSelectionDialog : StepCounterAction

    data class OnSaveStepGoal(val stepGoal: Int) : StepCounterAction

    data object OnClickPersonalSettingMenu: StepCounterAction
    
    data object OnClickPauseResume: StepCounterAction

    data object OnClickEditSteps: StepCounterAction
    data object OnDismissEditStepsDialog: StepCounterAction
    data class OnSaveEditSteps(val date: String, val time: String, val steps: String): StepCounterAction

    data object OnClickDate: StepCounterAction
    data object OnDismissDatePickerDialog: StepCounterAction
    data class OnDateSelected(val date: LocalDate): StepCounterAction

    data class OnResultRequestingNotificationPermission(val result: Boolean): StepCounterAction
}