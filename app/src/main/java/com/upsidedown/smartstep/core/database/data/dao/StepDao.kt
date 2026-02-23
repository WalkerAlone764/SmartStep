package com.upsidedown.smartstep.core.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.upsidedown.smartstep.core.database.data.entities.StepEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface StepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStep(step: StepEntity)

    @Query("SELECT * FROM steps WHERE date = :date")
    suspend fun getStepByDate(date: LocalDate): StepEntity?

    @Transaction
    suspend fun increaseStepCount(date: LocalDate, amount: Int) {
        val stepEntity = getStepByDate(date)
        upsertStep(
            StepEntity(
                date = date,
                count = (stepEntity?.count ?: 0) + amount
            )
        )
    }

    @Query("UPDATE steps SET count = :count WHERE date = :date")
    suspend fun updateStepsByDate(date: LocalDate, count: Int)

    @Query("UPDATE steps SET count = 0 WHERE date = :date")
    suspend fun resetStepsForDate(date: LocalDate)

    @Query("SELECT * FROM steps ORDER BY date DESC")
    fun getAllSteps(): Flow<List<StepEntity>>

    @Query("SELECT * FROM steps WHERE date = :date ORDER BY date DESC")
    fun getAllStepsSortedByDate(date: LocalDate): Flow<List<StepEntity>>

    @Query("SELECT * FROM steps WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getStepsInRange(startDate: LocalDate, endDate: LocalDate): Flow<List<StepEntity>>
}
