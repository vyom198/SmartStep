package com.vs.smartstep.core.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyStepDao {
    @Query("SELECT * FROM daily_steps ORDER BY date ASC LIMIT 7")
    fun getLast7DaysAscending(): Flow<List<DailyStep>>

    @Query("SELECT * FROM daily_steps WHERE date = :date")
    fun getDailyStepByDate(date: String): Flow<DailyStep>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyStep(dailyStep: DailyStep)





}