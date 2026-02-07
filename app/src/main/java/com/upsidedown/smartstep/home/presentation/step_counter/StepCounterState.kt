package com.upsidedown.smartstep.home.presentation.step_counter

import java.text.NumberFormat
import java.util.Locale

data class StepCounterState(
    val currentSteps: Int = 4563,
    val goalSteps: Int = 6000,
) {
    val formattedCurrentSteps: String
        get() = NumberFormat.getNumberInstance(Locale.US).format(currentSteps)
}