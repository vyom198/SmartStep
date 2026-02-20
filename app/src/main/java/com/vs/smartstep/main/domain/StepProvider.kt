package com.vs.smartstep.main.domain

import kotlinx.coroutines.flow.StateFlow

interface StepProvider {
    val steps: StateFlow<Int>
    fun startListening()
    fun stopListening()
}