package com.vs.smartstep.main.presentation.smartstep

sealed interface SmartStepHomeAction {
    data object DismissDialog : SmartStepHomeAction
    data object OpenAppSettings : SmartStepHomeAction
    data class UpdatePermissionStatus(val isGranted: Boolean) : SmartStepHomeAction
    data object ResetAfterSettings: SmartStepHomeAction

    data object  onHasActivityPermission : SmartStepHomeAction

    data object onClickContinueBackground : SmartStepHomeAction

    data object onClickFixCount : SmartStepHomeAction
    data object OnDisposed : SmartStepHomeAction
}


