package com.upsidedown.smartstep.home.presentation.step_counter

sealed interface StepCounterAction {

    data object OnResume: StepCounterAction
    data object OnClickExit: StepCounterAction

    data object OnClickOk: StepCounterAction
    data object OnDismissCloseDialog: StepCounterAction

    data class OnCheckPermissionResult(
        val activityRecognitionResult: Boolean,
        val backgroundLocationResult: Boolean
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
}