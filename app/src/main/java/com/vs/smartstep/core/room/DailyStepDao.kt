package com.vs.smartstep.core.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyStepDao {
    @Query("""
        SELECT * FROM daily_steps 
        WHERE date >= :startDate 
        AND date <= :endDate
        ORDER BY date Asc
    """)
    fun getStepsInDateRange(startDate: String, endDate: String): Flow<List<DailyStep>>

    @Query("SELECT * FROM daily_steps WHERE date = :date")
    fun getDailyStepByDateFlow(date: String): Flow<DailyStep?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyStep(dailyStep: DailyStep)
    @Query("SELECT * FROM daily_steps WHERE date = :date")
    suspend fun getDailyStepByDate(date: String): DailyStep?

    @Query("UPDATE daily_steps SET timeTaken = timeTaken + :additionalTime WHERE date = :date")
    suspend fun addTime(additionalTime: Long, date: String)

    @Query("UPDATE daily_steps SET stepGoal = :step WHERE date = :date")
    suspend fun saveGaol(step : Int , date: String)

}