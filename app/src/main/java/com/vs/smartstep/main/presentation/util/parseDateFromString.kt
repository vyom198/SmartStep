package com.vs.smartstep.main.presentation.util

fun parseDateFromString(dateStr: String): Triple<Int, Int, Int> {
    return try {
        val parts = dateStr.split("/")
        Triple(
            parts[0].toInt(), // year
            parts[1].toInt(), // month
            parts[2].toInt()  // day
        )
    } catch (e: Exception) {
        // Default if parsing fails
        Triple(2025, 1, 1)
    }
}