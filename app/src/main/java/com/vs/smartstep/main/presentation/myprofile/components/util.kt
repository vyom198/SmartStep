package com.vs.smartstep.main.presentation.myprofile.components

import kotlin.math.floor
import kotlin.math.round
import kotlin.math.roundToInt

fun getformattedHeight(index : Int , heightIncm : Int):String{
    return when(index){
        0 -> "$heightIncm cm"
        else -> {
            val totalInches = heightIncm / 2.54
            var feet = floor(totalInches / 12).toInt()
            var inches = round(totalInches % 12).toInt()

            // Handle inches rounding to 12
            if (inches == 12) {
                feet += 1
                inches = 0
            }

            // Format based on values
            when {
                feet > 0 && inches > 0 -> "${feet}ft ${inches}in"
                feet > 0 -> "${feet}ft"
                else -> "${inches}in"
            }
        }
    }
}

fun lbsFromKg(kg: Int): Int {
    return (kg * 2.20462).roundToInt()
}

fun kgFromLbs(lbs: Int): Int {
    return (lbs / 2.20462).roundToInt()
}