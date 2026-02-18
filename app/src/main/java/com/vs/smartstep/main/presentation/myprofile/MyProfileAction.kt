package com.vs.smartstep.main.presentation.myprofile

sealed interface MyProfileAction {
    data class onSelectHeight(val index : Int , val heightIncm : Double) : MyProfileAction
    object onDismissHeightDialog : MyProfileAction

    object  selectingHeight : MyProfileAction

    data class onSelectWeight(val index : Int , val weight : Double) : MyProfileAction
    object onDismissWeightDialog : MyProfileAction

    object  selectingWeight : MyProfileAction

    object  onSave : MyProfileAction

    data class onSelectingGender(val gender : String) : MyProfileAction

}