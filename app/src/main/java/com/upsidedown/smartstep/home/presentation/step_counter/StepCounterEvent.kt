package com.upsidedown.smartstep.home.presentation.step_counter

sealed interface StepCounterEvent {
    data object ExitApp: StepCounterEvent
    data object CheckAllPermission: StepCounterEvent
    data object OpenSetting: StepCounterEvent
    data object RequestPhysicalActivityPermission: StepCounterEvent
    data object CheckCanShowRequestPhysicalActivityPermission: StepCounterEvent
    data object RequestForBatteryOptimization : StepCounterEvent
    data object RequestNotificationPermission: StepCounterEvent
    
    data object StartStepCounterService: StepCounterEvent
    data object StopStepCounterService: StepCounterEvent
}