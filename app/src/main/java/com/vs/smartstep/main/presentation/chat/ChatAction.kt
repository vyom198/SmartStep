package com.vs.smartstep.main.presentation.chat

sealed interface ChatAction {
   data object ToogleSuggestion : ChatAction
   data class ontextChange (val text : String) : ChatAction
   data class SendMessage(val text: String) : ChatAction
}