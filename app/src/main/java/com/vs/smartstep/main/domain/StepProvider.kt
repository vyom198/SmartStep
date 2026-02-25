package com.vs.smartstep.main.domain

import kotlinx.coroutines.flow.StateFlow

interface StepProvider {
    fun startListening()
    fun stopListening()
}