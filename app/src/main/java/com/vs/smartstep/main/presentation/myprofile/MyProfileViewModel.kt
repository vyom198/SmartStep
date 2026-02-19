package com.vs.smartstep.main.presentation.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vs.smartstep.main.domain.userProfileStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class MyProfileViewModel(
    private val userProfileStore: userProfileStore
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MyProfileState())
    private val eventChannel = Channel<MyProfileEvent>()
    val events = eventChannel.receiveAsFlow()
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
               loadData()
                fillData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5_000L),
            initialValue = MyProfileState()
        )

    private fun loadData(){
        viewModelScope.launch {
            userProfileStore.isProfileSetup().collect { bool ->
                _state.update {
                    it.copy(
                        dataNotNull = bool

                    )
                }
            }
        }
    }
    private fun fillData(){
        viewModelScope.launch {
            val gender = userProfileStore.getGender()
            val height = userProfileStore.getHeightWithUnit()
            val weight = userProfileStore.getWeightWithUnit()
            _state.update {
                it.copy(
                    currentGender = gender,
                    selectedUnitforHeight = height.first,
                    selectedHeightInCm = height.second,
                    selectedUnitforWeight = weight.first,
                    selectedWeight = weight.second
                )
            }
        }
    }
    private fun storeData(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                userProfileStore.saveGender(_state.value.currentGender)
                userProfileStore.saveWeightWithUnit(_state.value.selectedUnitforWeight ,_state.value.selectedWeight )
                userProfileStore.saveheightWithUnit(_state.value.selectedUnitforHeight ,_state.value.selectedHeightInCm )
            }
            withContext(Dispatchers.Main){
                eventChannel.send(MyProfileEvent.OnSaved)

            }
        }
    }
    fun onAction(action: MyProfileAction) {
        when (action) {
            is MyProfileAction.onSelectHeight -> {
                viewModelScope.launch {
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

            MyProfileAction.onSave -> storeData()

            is MyProfileAction.onSelectingGender -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            currentGender = action.gender
                        )
                    }
                }
            }
        }
    }

}