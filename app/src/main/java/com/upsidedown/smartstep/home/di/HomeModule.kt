package com.upsidedown.smartstep.home.di

import com.upsidedown.smartstep.home.data.AndroidStepDataSource
import com.upsidedown.smartstep.home.data.DataStoreStepCounterSettingDataSource
import com.upsidedown.smartstep.home.data.StepRepositoryImpl
import com.upsidedown.smartstep.home.domain.StepCounterSettingDataSource
import com.upsidedown.smartstep.home.domain.StepDataSource
import com.upsidedown.smartstep.home.domain.StepRepository
import com.upsidedown.smartstep.home.presentation.step_counter.StepCounterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    single<StepDataSource> { AndroidStepDataSource(get(), get(), get()) }
    single<StepCounterSettingDataSource> { DataStoreStepCounterSettingDataSource(androidContext()) }

    singleOf(::StepRepositoryImpl) bind StepRepository::class

    viewModelOf(::StepCounterViewModel)
}