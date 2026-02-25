package com.vs.smartstep.core.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [DailyStep::class],
    version = 4,
)
abstract class DailyStepDatabase: RoomDatabase() {
    abstract val dailyStepDao : DailyStepDao
}