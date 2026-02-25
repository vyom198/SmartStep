package com.vs.smartstep.main.presentation.util

import com.vs.smartstep.main.presentation.myprofile.components.kgFromLbs
import kotlin.math.roundToInt

fun calculateDistance(
    steps: Int,
    heightCm: Double,
     Unit : Int
):Double {
    val isMetric = Unit == 0
    val stepLengthCm = heightCm * 0.415
    val distanceMeters = steps * stepLengthCm / 100

    val distanceValue = if (isMetric) {
        distanceMeters / 1000
    } else {
        distanceMeters / 1609.34
    }
    val roundedValue = (distanceValue * 10.0).roundToInt() / 10.0
    
    return roundedValue
}
fun Long.toMinutesRounded(): Int =
    (this / (1000f * 60f)).roundToInt()
fun calculateCalories(
    steps: Int,
    weight: Int ,
    gender : String ,
    unit : Int
): Int {
    val weightINkg = if(unit == 0) weight else kgFromLbs(weight)
    val genderFactor = if(gender == "Male") 1 else 0.9
    val kcalPerStep = weightINkg * 0.0005 * genderFactor.toDouble()
    val total_kcal = kcalPerStep * steps
   return total_kcal.roundToInt()

}