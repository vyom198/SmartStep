package com.vs.smartstep.main.presentation.util

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

fun getTodayDate() : String {
    val dateFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("yyyy/MM/dd")
    } else {
        null
    }
    val todayDate by if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        mutableStateOf(LocalDate.now().format(dateFormatter))
    } else {
        val calendar = Calendar.getInstance()
        val legacyFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        mutableStateOf(legacyFormat.format(calendar.time))
    }
    return todayDate
}