package com.vs.smartstep.di

import androidx.room.Room
import com.vs.smartstep.core.room.DailyStepDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val testFeatureModule = module {
    single<DailyStepDatabase> {
        Room.inMemoryDatabaseBuilder(
            androidApplication(),
            DailyStepDatabase::class.java
        ).build()
    }
    single {
        get<DailyStepDatabase>().dailyStepDao
    }
    single<CoroutineScope> {
        TestScope(UnconfinedTestDispatcher())
    }


}