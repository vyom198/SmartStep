package com.vs.smartstep.main.presentation.smartstep

data class SmartStepHomeState(
    val shouldShowAllow: Boolean = false,
    val hasActivityPermission :Boolean = false,
    val count : Int = 0,
    val openSettings : Boolean = false,
    val showBackgroundRationale : Boolean = false,
    val isIgnoringBatteryOpti : Boolean = false,
    val stepGoalBS :Boolean = false,
    val dailyGoal : Int = 0,
    val stepCount : Int = 0,
    val exitDialog : Boolean = false

){
    val progress: Float
        get() = (stepCount.toFloat() / dailyGoal.coerceAtLeast(1)).coerceIn(0f, 1f)
}