package com.upsidedown.smartstep.home.presentation.step_counter

sealed interface StepCounterAction {
    data object OnClickExit: StepCounterAction

    data object OnClickOk: StepCounterAction
    data object OnDismissCloseDialog: StepCounterAction
}