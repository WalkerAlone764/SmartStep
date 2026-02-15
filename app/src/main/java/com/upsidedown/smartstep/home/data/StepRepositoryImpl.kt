package com.upsidedown.smartstep.home.data

import com.upsidedown.smartstep.core.database.data.dao.StepDao
import com.upsidedown.smartstep.core.database.data.mapper.toStep
import com.upsidedown.smartstep.core.database.domain.model.Step
import com.upsidedown.smartstep.home.domain.StepDataSource
import com.upsidedown.smartstep.home.domain.StepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class StepRepositoryImpl(
    private val stepDao: StepDao,
    private val stepDataSource: StepDataSource
): StepRepository {


    override fun getAllStepsByDate(date: LocalDate): Flow<List<Step>> {
        return stepDao.getAllStepsSortedByDate(date).map { it.map { it.toStep() } }
    }

}