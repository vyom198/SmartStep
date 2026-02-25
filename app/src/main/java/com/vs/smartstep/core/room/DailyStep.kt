package com.vs.smartstep.core.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_steps")
data class DailyStep(
    @PrimaryKey
    val date: String,
    val steps: Int,
    val stepGoal : Int ,
    val kcal : Int = 0  ,
    val baseline: Int =0
)
