package com.vs.smartstep.main.presentation.smartstep

import com.vs.smartstep.core.room.DailyStep
import com.vs.smartstep.main.presentation.util.getDayOfWeekFromDate

data class DailyActivityUI(
    val day : String,
    val steps: Int,
    val stepGoal: Int,
)

fun DailyStep.toUI() : DailyActivityUI {
    return DailyActivityUI(
        day = getDayOfWeekFromDate(this.date),
        steps = this.steps,
        stepGoal = this.stepGoal
    )
}
