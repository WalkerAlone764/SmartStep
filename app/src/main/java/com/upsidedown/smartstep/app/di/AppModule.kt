package com.upsidedown.smartstep.app.di

import com.upsidedown.smartstep.app.SmartStepApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {

    single<CoroutineScope> {
        (androidApplication() as SmartStepApp).applicationScope
    }
}