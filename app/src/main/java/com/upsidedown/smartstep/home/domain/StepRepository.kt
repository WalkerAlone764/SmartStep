package com.upsidedown.smartstep.home.domain

import com.upsidedown.smartstep.core.database.domain.model.Step
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface StepRepository {

    fun getAllStepsByDate(date: LocalDate): Flow<List<Step>>
}