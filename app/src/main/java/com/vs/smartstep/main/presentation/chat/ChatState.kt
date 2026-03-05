package com.vs.smartstep.main.presentation.chat

data class ChatState(
   val isSuggestion : Boolean = false,
   val query : String = "",
   val messages: List<ChatMessage> = emptyList(),
   val isInternetAvailable : Boolean = false
)
data class ChatMessage(
   val content: String,
   val sender: Sender,
)
enum class Sender {
   USER,
   AI
}