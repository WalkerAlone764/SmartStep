package com.upsidedown.smartstep.core.database.data.dao

import android.util.Log
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

    @Query("UPDATE steps SET count = count + :amount WHERE date = :date")
    suspend fun updateStepCount(date: LocalDate, amount: Int): Int

    @Transaction
    suspend fun increaseStepCount(date: LocalDate, amount: Int) {
        val stepEntity = getStepByDate(date)

        Log.d("stepEntity", stepEntity.toString())
        upsertStep(
            StepEntity(
                date = date,
                count = (stepEntity?.count ?: 0) + amount
            )
        )

    }

    @Query("SELECT * FROM steps ORDER BY date DESC")
    fun getAllSteps(): Flow<List<StepEntity>>

    @Query("SELECT * FROM steps WHERE date = :date ORDER BY date DESC")
    fun getAllStepsSortedByDate(date: LocalDate): Flow<List<StepEntity>> = getAllSteps()
}
