package com.vs.smartstep.data

import com.vs.smartstep.main.domain.smartStep.StepProvider

class FakeStepProvider : StepProvider {
    var isListening = false
        private set

    override fun startListening() {
        isListening = true
    }

    override fun stopListening() {
        isListening = false
    }

    // Helper for testing: simulate a sensor event
    fun simulateStepDetected() {
        if (isListening) {
            // In a real integration test, you'd likely have this 
            // call a function that updates the Room DB directly 
            // to mimic the StepDetector's behavior.
        }
    }
}