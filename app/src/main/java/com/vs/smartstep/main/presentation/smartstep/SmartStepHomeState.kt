package com.vs.smartstep.main.presentation.smartstep

data class SmartStepHomeState(
    val shouldShowAllow: Boolean = false,
    val hasActivityPermission :Boolean = false,
    val count : Int = 0,
    val openSettings : Boolean = false,
    val showBackgroundRationale : Boolean = false,
    val isIgnoringBatteryOpti : Boolean = false

)