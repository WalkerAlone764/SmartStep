package com.upsidedown.smartstep.home.presentation.step_counter

sealed interface StepCounterEvent {
    data object ExitApp: StepCounterEvent
}