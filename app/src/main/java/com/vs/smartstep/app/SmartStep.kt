package com.vs.smartstep.app

import android.app.Application
import com.vs.smartstep.BuildConfig
import com.vs.smartstep.main.di.mainModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber


class SmartStep : Application() {
    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidContext(this@SmartStep)
            modules(
                appModule,
                mainModule

            )
        }
    }
}