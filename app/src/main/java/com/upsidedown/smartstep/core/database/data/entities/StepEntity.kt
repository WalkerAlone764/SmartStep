package com.upsidedown.smartstep.core.database.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "steps")
data class StepEntity(
    @PrimaryKey(autoGenerate = false)
    val date: LocalDate,
    val count: Int,
)