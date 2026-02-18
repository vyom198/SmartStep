package com.vs.smartstep.main.domain

import kotlinx.coroutines.flow.Flow

interface userProfileStore {
    suspend fun getWeightWithUnit() : Pair<Int , Int >
   suspend fun getGender() : String
    suspend fun getHeightWithUnit() : Pair<Int , Int >

    suspend fun  saveWeightWithUnit (unit : Int , weight :Int)
    suspend fun  saveGender(gender : String )
    suspend fun  saveheightWithUnit ( unit : Int , height : Int)


      fun isProfileSetup(): Flow<Boolean>
}