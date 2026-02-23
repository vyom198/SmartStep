package com.vs.smartstep.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.vs.smartstep.core.room.DailyStep
import com.vs.smartstep.core.room.DailyStepDao
import com.vs.smartstep.core.room.DailyStepDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import kotlin.jvm.java

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")
val coreModule = module{
    single<DataStore<Preferences>> {
        androidContext().dataStore
    }
    single<DailyStepDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            DailyStepDatabase::class.java,
            "steps.db",
        ).fallbackToDestructiveMigration(true).build()
    }
    single {
        get<DailyStepDatabase>().dailyStepDao
    }


}