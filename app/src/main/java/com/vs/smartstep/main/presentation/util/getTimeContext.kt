package com.vs.smartstep.main.presentation.util

import android.os.Build
import java.time.LocalTime
import java.util.Calendar

fun getTimeContext(): String {
    val currentHour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalTime.now().hour
    } else {
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    return when (currentHour) {
        in 5..11 -> "morning"
        in 12..16 -> "noon"
        in 17..20 -> "evening"
        else -> "night"
    }
}