package com.vs.smartstep.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")
val coreModule = module{
    single<DataStore<Preferences>> {
        androidContext().dataStore
    }

}