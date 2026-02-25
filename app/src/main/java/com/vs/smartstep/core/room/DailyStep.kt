package com.vs.smartstep.core.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_steps")
data class DailyStep(
    @PrimaryKey
    val date: String,
    val steps: Int = 0,
    val stepGoal : Int = 0 ,
    val distance : Double = 0.0 ,
    val kcal : Int = 0 ,
    val lastSensorValue : Long = 0,
    val baseline: Long =0,
    val manualSteps : Int = 0,
    val timeTaken : Long = 0
)
