package com.vs.smartstep.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.vs.smartstep.BuildConfig
import com.vs.smartstep.core.di.coreModule
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
                mainModule,
                coreModule

            )
        }
        createNotificationChannel()
    }
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "step",
                "show_steps",
                NotificationManager.IMPORTANCE_MIN
            )
            channel.description = "Used for Running Step Service"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}