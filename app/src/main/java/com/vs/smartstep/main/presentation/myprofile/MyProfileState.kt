package com.vs.smartstep.main.presentation.myprofile

data class MyProfileState(
   val selectedUnitforHeight : Int = 0,
    val selectedHeightInCm : Int = 175,
    val isHeightDialog : Boolean = false,
   val selectedUnitforWeight : Int = 0,
   val selectedWeight : Int = 65,
   val isWeightDialog : Boolean = false,
    val currentGender : String = "Female"
)