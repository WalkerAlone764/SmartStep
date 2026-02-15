package com.upsidedown.smartstep.core.database.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.upsidedown.smartstep.core.database.data.dao.StepDao
import com.upsidedown.smartstep.core.database.data.entities.StepEntity
import com.upsidedown.smartstep.core.database.data.room_converter.LocalDateConverter

@Database(
    entities = [StepEntity::class],
    version = 1
)
@TypeConverters(LocalDateConverter::class)
abstract class StepDatabase : RoomDatabase() {
    abstract val dao: StepDao
}