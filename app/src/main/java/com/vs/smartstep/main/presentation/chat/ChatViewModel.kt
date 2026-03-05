package com.vs.smartstep.main.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vs.smartstep.main.domain.smartStep.ConnectionProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val connectionProvider: ConnectionProvider
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ChatState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                 checkInternet()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatState()
        )
    private fun checkInternet(){
        viewModelScope.launch {
            connectionProvider.isConnectedFlow.collect { connected ->
                _state.update {
                    it.copy(
                        isInternetAvailable = connected
                    )
                }
            }
        }
    }
    fun onAction(action: ChatAction) {
        when (action) {
            ChatAction.ToogleSuggestion -> {
                _state.update {
                    it.copy(
                        isSuggestion = !it.isSuggestion
                    )
                }
            }

            is ChatAction.ontextChange -> {
                _state.update {
                    it.copy(
                        query = action.text
                    )
                }
            }

            ChatAction.SendInitialMessage -> TODO()
            is ChatAction.SendMessage -> {

            }
        }
    }

}