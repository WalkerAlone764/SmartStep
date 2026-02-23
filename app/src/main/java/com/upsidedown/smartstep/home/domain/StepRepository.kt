package com.upsidedown.smartstep.home.domain

import com.upsidedown.smartstep.core.database.domain.model.Step
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface StepRepository {

    val isPaused: Flow<Boolean>

    suspend fun setPaused(isPaused: Boolean)

    fun getAllStepsByDate(date: LocalDate): Flow<List<Step>>

    fun getStepsInRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Step>>

    suspend fun increaseStepCount(date: LocalDate, count: Int)

    suspend fun updateStepsByDate(date: LocalDate, count: Int)

    suspend fun resetStepsForDate(date: LocalDate)
}
