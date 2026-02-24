package com.upsidedown.smartstep.home.presentation.step_counter

import androidx.compose.foundation.text.input.TextFieldState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class StepCounterState(
    val currentSteps: Int = 0,
    val goalSteps: Int = 6000,
    val isPaused: Boolean = false,
    val distance: Double = 0.0,
    val distanceUnit: String = "km",
    val calories: Int = 0,
    val timeMin: Int = 0,
    val hasActivityRecognitionPermission: Boolean = false,
    val hasIgnoreBatteryOptimizationPermission: Boolean = false,
    val hasNotificationPermission: Boolean = false,
    val isActivityRecognitionPermissionRationaleDialogShown: Boolean = false,
    val isActivityRecognitionPermissionSettingDialogShown: Boolean = false,
    val isIgnoreBatteryOptimizationDialogShown: Boolean = false,
    val isExitDialogShown: Boolean = false,
    val isStepGoalSelectionDialogShown: Boolean = false,
    val isEditStepsDialogShown: Boolean = false,
    val stepsInEdit: TextFieldState = TextFieldState(),
    val isDatePickerDialogShown: Boolean = false,
    val selectedDateInEdit: LocalDate = LocalDate.now(),
    val pastSevenDaysSteps: List<Map<String, Any>> = emptyList()
) {
    val formattedCurrentSteps: String
        get() = String.format("%,d", currentSteps)
        
    val formattedSelectedDate: String
        get() = selectedDateInEdit.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
}