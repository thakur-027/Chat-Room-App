package com.example.chatroomapp.data

data class Message(
    val senderId: String = "",
    val senderFirstName: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isSentByCurrentUser: Boolean = false // Logic to determine this will be in ViewModel
)