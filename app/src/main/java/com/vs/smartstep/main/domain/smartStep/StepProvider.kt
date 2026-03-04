package com.vs.smartstep.main.domain.smartStep

interface StepProvider {
    fun startListening()
    fun stopListening()
}