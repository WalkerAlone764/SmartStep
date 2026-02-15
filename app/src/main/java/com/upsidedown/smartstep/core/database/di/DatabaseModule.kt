package com.upsidedown.smartstep.core.database.di

import androidx.room.Room
import com.upsidedown.smartstep.core.database.data.StepDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            StepDatabase::class.java,
            "smart_step.db"
        ).build()
    }
    single { get<StepDatabase>().dao }
}