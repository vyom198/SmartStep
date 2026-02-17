package com.vs.smartstep.main.presentation.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt

class MyProfileViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MyProfileState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5_000L),
            initialValue = MyProfileState()
        )

    fun onAction(action: MyProfileAction) {
        when (action) {
            is MyProfileAction.onSelectHeight -> {
                _state.update {
                    it.copy(
                        selectedUnitforHeight = action.index,
                        selectedHeightInCm = action.heightIncm.toInt(),
                        isHeightDialog = false
                    )
                }
               if(action.index == 0 && _state.value.selectedUnitforWeight == 1 ){
                   val newWeight = (_state.value.selectedWeight / 2.20462).roundToInt()
                   _state.update {
                       it.copy(
                           selectedWeight = newWeight,
                           selectedUnitforWeight = 0
                       )
                   }
               }
                if(action.index == 1 && _state.value.selectedUnitforWeight == 0 ){
                    val newWeight = (_state.value.selectedWeight * 2.20462).roundToInt()
                    _state.update {
                        it.copy(
                            selectedWeight = newWeight,
                            selectedUnitforWeight = 1
                        )
                    }
                }
            }
            MyProfileAction.onDismissHeightDialog -> {
             _state.update {
                 it.copy(
                     isHeightDialog = false
                 )
             }

            }

            MyProfileAction.selectingHeight -> {
                _state.update {
                    it.copy(
                        isHeightDialog = true
                    )
                }

            }

            MyProfileAction.onDismissWeightDialog -> {
                _state.update {
                    it.copy(
                        isWeightDialog = false
                    )
                }
            }
            is MyProfileAction.onSelectWeight -> {
                _state.update {
                    it.copy(
                        selectedUnitforWeight = action.index,
                        selectedWeight = action.weight.toInt(),
                        selectedUnitforHeight = action.index,
                        isWeightDialog = false

                    )
                }

            }
            MyProfileAction.selectingWeight -> {
                _state.update {
                    it.copy(
                        isWeightDialog = true
                    )
                }
            }
        }
    }

}