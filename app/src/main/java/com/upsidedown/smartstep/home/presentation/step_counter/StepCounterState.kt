package com.upsidedown.smartstep.home.presentation.step_counter

import java.text.NumberFormat
import java.util.Locale

data class StepCounterState(
    val currentSteps: Int = 0,
    val goalSteps: Int = 6000,
    val distanceKm: Double = 0.0,
    val calories: Int = 0,
    val timeMin: Int = 0,
    val isPaused: Boolean = false,
    val isExitDialogShown: Boolean = false,
    val hasActivityRecognitionPermission: Boolean = false,
    val hasIgnoreBatteryOptimizationPermission: Boolean = false,
    val isActivityRecognitionPermissionRationaleDialogShown: Boolean = false,
    val isActivityRecognitionPermissionSettingDialogShown: Boolean = false,
    val isIgnoreBatteryOptimizationDialogShown: Boolean = false,
    val isStepGoalSelectionDialogShown: Boolean = false
) {
    val formattedCurrentSteps: String
        get() = NumberFormat.getNumberInstance(Locale.US).format(currentSteps)
}