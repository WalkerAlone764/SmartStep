package com.upsidedown.smartstep.app

import android.app.Application
import com.upsidedown.smartstep.app.di.appModule
import com.upsidedown.smartstep.profile.di.profileModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SmartStepApp: Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SmartStepApp)
            modules(appModule,profileModule)
        }
    }
}