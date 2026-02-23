package com.vs.smartstep.core.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [DailyStep::class],
    version = 1,
)
abstract class DailyStepDatabase: RoomDatabase() {
    abstract val dailyStepDao : DailyStepDao
}