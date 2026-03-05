package com.vs.smartstep.main.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vs.smartstep.main.domain.smartStep.AIInsights
import com.vs.smartstep.main.domain.smartStep.ConnectionProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val connectionProvider: ConnectionProvider,
    private val aiInsights: AIInsights
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ChatState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                 checkInternet()
                  getInitialMessage()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatState()
        )

    private fun getInitialMessage(){
        viewModelScope.launch(Dispatchers.IO) {
            val message = async {   aiInsights.getInitialMessage()}.await()

          withContext(Dispatchers.Main) {
              _state.update {
                  it.copy(
                      messages = it.messages + ChatMessage(
                          content = message,
                          sender = Sender.AI
                      )
                  )
              }
          }
        }
    }
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

            is ChatAction.SendMessage -> {
               viewModelScope.launch {
                 withContext(Dispatchers.Main) {
                     _state.update {
                         it.copy(
                             messages = it.messages + ChatMessage(
                                 content = action.text,
                                 sender = Sender.USER
                             )
                         )
                     }
                 }
                       withContext(Dispatchers.IO) {
                       val reply = aiInsights.chatWithAI(action.text)
                       withContext(Dispatchers.Main) {
                           _state.update {
                               it.copy(
                                   messages = it.messages + ChatMessage(
                                       content = reply,
                                       sender = Sender.AI
                                   )
                               )
                           }
                       }
                   }
               }
            }
        }
    }

}