package com.vs.smartstep.main.presentation.util

import java.text.NumberFormat
import java.util.Locale

fun Int.toCommaString(): String {
    return NumberFormat.getInstance(Locale.US).format(this)
}