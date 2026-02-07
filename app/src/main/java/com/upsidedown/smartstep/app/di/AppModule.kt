package com.upsidedown.smartstep.app.di

import com.upsidedown.smartstep.app.MainViewModel
import com.upsidedown.smartstep.app.SmartStepApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single<CoroutineScope> {
        (androidApplication() as SmartStepApp).applicationScope
    }

    viewModelOf(::MainViewModel)
}