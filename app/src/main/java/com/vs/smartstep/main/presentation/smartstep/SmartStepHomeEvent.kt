package com.vs.smartstep.main.presentation.smartstep

interface SmartStepHomeEvent {
    data class Granted(val hasPermission : Boolean): SmartStepHomeEvent
    object TerminateApp : SmartStepHomeEvent

}