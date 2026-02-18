package com.vs.smartstep.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vs.smartstep.main.domain.userProfileStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class userProfileStoreImpl(
    private val dataStore: DataStore<Preferences>
) : userProfileStore {
    private object Keys{
        val GENDER = stringPreferencesKey("gender")

        // Weight
        val WEIGHT_UNIT = intPreferencesKey("weight_unit") // 0 = kg, 1 = lb
        val WEIGHT_VALUE = intPreferencesKey("weight_value")

        // Height
        val HEIGHT_UNIT = intPreferencesKey("height_unit") // 0 = cm, 1 = ft/in
        val HEIGHT_VALUE = intPreferencesKey("height_value")

        val SAVED = booleanPreferencesKey("saved")

    }


    override suspend fun getWeightWithUnit(): Pair<Int, Int> {
        val preferences = dataStore.data.first()
        val unit = preferences[Keys.WEIGHT_UNIT] ?: 0
        val value = preferences[Keys.WEIGHT_VALUE] ?: 0
        return Pair(unit, value)
    }

    override suspend fun getGender(): String {
        val preferences = dataStore.data.first()
        return preferences[Keys.GENDER] ?: ""
    }

    override suspend fun getHeightWithUnit(): Pair<Int, Int>{
        val preferences = dataStore.data.first()
        val unit = preferences[Keys.HEIGHT_UNIT] ?: 0
        val value = preferences[Keys.HEIGHT_VALUE] ?: 0
        return Pair(unit, value)
    }

    override suspend fun saveWeightWithUnit(unit: Int, weight: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.WEIGHT_UNIT] = unit
            preferences[Keys.WEIGHT_VALUE] = weight
        }
    }

    override suspend fun saveGender(gender: String) {
        dataStore.edit { preferences ->
            preferences[Keys.GENDER] = gender
        }
    }

    override suspend fun saveheightWithUnit(unit: Int, height: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.HEIGHT_UNIT] = unit
            preferences[Keys.HEIGHT_VALUE] = height
        }
    }

    override fun isProfileSetup(): Flow<Boolean> = dataStore.data
        .map { preferences ->
            // Check if a required key (like gender or weight) is not null/empty
            val gender = preferences[Keys.GENDER]
            !gender.isNullOrBlank()
        }


}