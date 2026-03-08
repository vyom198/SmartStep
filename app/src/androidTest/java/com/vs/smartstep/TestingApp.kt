package com.vs.smartstep

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import com.vs.smartstep.di.testFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class TestingApp: Application() {
        override fun onCreate() {
            super.onCreate()
            startKoin {
                androidContext(
                    InstrumentationRegistry.getInstrumentation()
                        .targetContext.applicationContext
                )
                modules(testFeatureModule)
            }
        }
}
