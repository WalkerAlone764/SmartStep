package com.upsidedown.smartstep.home.data

import com.upsidedown.smartstep.core.database.data.dao.StepDao
import com.upsidedown.smartstep.core.database.data.mapper.toStep
import com.upsidedown.smartstep.core.database.domain.model.Step
import com.upsidedown.smartstep.home.domain.StepCounterSettingDataSource
import com.upsidedown.smartstep.home.domain.StepDataSource
import com.upsidedown.smartstep.home.domain.StepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class StepRepositoryImpl(
    private val stepDao: StepDao,
    private val stepDataSource: StepDataSource,
    private val stepCounterSettingDataSource: StepCounterSettingDataSource
): StepRepository {

    override val isPaused: Flow<Boolean>
        get() = stepCounterSettingDataSource.isPaused

    override suspend fun setPaused(isPaused: Boolean) {
        stepCounterSettingDataSource.setPaused(isPaused)
    }

    override fun getAllStepsByDate(date: LocalDate): Flow<List<Step>> {
        return stepDao.getAllStepsSortedByDate(date).map { it.map { it.toStep() } }
    }

    override suspend fun increaseStepCount(date: LocalDate, count: Int) {
        return stepDao.increaseStepCount(date, count)
    }

    override suspend fun updateStepsByDate(date: LocalDate, count: Int) {
        stepDao.updateStepsByDate(date, count)
    }

    override suspend fun resetStepsForDate(date: LocalDate) {
        return stepDao.resetStepsForDate(date)
    }

}
