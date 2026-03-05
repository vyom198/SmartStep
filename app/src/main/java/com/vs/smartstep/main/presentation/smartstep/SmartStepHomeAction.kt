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

    data object stepGoalBottomSheet : SmartStepHomeAction
    data object onDismissBackgroundAccess : SmartStepHomeAction
    data class saveStep(val steps : Int) : SmartStepHomeAction

    data object  startSensor : SmartStepHomeAction

    data object  onExitOrDismissClick : SmartStepHomeAction
    data object onExitConfirm : SmartStepHomeAction

    data object dismissOpenApp : SmartStepHomeAction


    data object  isEditingDialog : SmartStepHomeAction

    data object onResseting : SmartStepHomeAction
    data object onPlayPause : SmartStepHomeAction
    data object onResetConfirm : SmartStepHomeAction


    data object onReload : SmartStepHomeAction
    data class onSaveDate ( val date : String , val steps : Int) : SmartStepHomeAction
}


