package com.vs.smartstep.main.presentation.util

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
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

fun getDaysAgoDate( daysBefore: Long): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // API 26+ use LocalDate
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val sixDaysAgo = LocalDate.now().minusDays(daysBefore)
        sixDaysAgo.format(dateFormatter)
    } else {
        // Below API 26 use Calendar
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysBefore.toInt())
        val legacyFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        legacyFormat.format(calendar.time)
    }
}

fun getDayOfWeekFromDate(dateStr: String, locale: Locale = Locale.getDefault()): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // API 26+ use LocalDate
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val date = LocalDate.parse(dateStr, formatter)
        date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
    } else {
        // Below API 26 use Calendar
        val format = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        val date = format.parse(dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // Convert Calendar day to string
        when (dayOfWeek) {
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            Calendar.SATURDAY -> "Sat"
            else -> ""
        }
    }
}