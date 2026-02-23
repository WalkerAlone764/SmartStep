package com.upsidedown.smartstep.home.presentation.step_counter

import androidx.compose.foundation.text.input.TextFieldState
import com.upsidedown.smartstep.core.database.domain.model.Step
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale

data class StepCounterState(
    val currentSteps: Int = 0,
    val goalSteps: Int = 6000,
    val distance: Double = 0.0,
    val distanceUnit: String = "km",
    val calories: Int = 0,
    val timeMin: Int = 0,
    val isPaused: Boolean = false,
    val isExitDialogShown: Boolean = false,
    val hasActivityRecognitionPermission: Boolean = false,
    val hasIgnoreBatteryOptimizationPermission: Boolean = false,
    val isActivityRecognitionPermissionRationaleDialogShown: Boolean = false,
    val isActivityRecognitionPermissionSettingDialogShown: Boolean = false,
    val isIgnoreBatteryOptimizationDialogShown: Boolean = false,
    val isStepGoalSelectionDialogShown: Boolean = false,
    val isEditStepsDialogShown: Boolean = false,
    val isDatePickerDialogShown: Boolean = false,
    val selectedDateInEdit: LocalDate = LocalDate.now(),
    val stepsInEdit: TextFieldState = TextFieldState(initialText = "0"),
    val pastSevenDaysSteps: List<Map<String, Any>> = emptyList()
) {
    val formattedCurrentSteps: String
        get() = NumberFormat.getNumberInstance(Locale.US).format(currentSteps)

    val formattedSelectedDate: String
        get() = selectedDateInEdit.toString().replace("-", "/")
}
