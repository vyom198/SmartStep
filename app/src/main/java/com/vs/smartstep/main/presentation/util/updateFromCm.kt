package com.vs.smartstep.main.presentation.util

import kotlin.math.floor
import kotlin.math.round

fun CmToFeet (cm: Double) : Pair<Int , Int> {

        val totalInches = cm / 2.54
        var feet = floor(totalInches / 12).toInt()
        var inches = round(totalInches % 12).toInt()
        if (inches == 12) {
            feet += 1
            inches = 0
        }
       return Pair(feet , inches)
    }