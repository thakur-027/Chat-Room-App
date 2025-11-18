package com.example.chatroomapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatroomapp.data.Injection
import com.example.chatroomapp.data.Message
import com.example.chatroomapp.data.MessageRepository
import com.example.chatroomapp.data.Result
import com.example.chatroomapp.data.User
import com.example.chatroomapp.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {

    private val messageRepository: MessageRepository
    private val userRepository: UserRepository

    init {
        messageRepository = MessageRepository(Injection.instance())
        userRepository = UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
        loadCurrentUser()
    }

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _roomId = MutableLiveData<String>()
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    // --- FIX: Add this property ---
    val currentUserEmail: String
        get() = FirebaseAuth.getInstance().currentUser?.email ?: ""

    fun setRoomId(roomId: String) {
        _roomId.value = roomId
        loadMessages()
    }

    fun sendMessage(text: String) {
        // 1. Try to get the detailed user from Firestore
        val firestoreUser = _currentUser.value

        // 2. If that's null, try to get the basic Auth user
        val authUser = FirebaseAuth.getInstance().currentUser

        if (firestoreUser != null) {
            // Scenario A: We have full user details
            val message = Message(
                senderFirstName = firestoreUser.firstName,
                senderId = firestoreUser.email,
                text = text
            )
            sendMessageToRepository(message)
        } else if (authUser != null) {
            // Scenario B: Fallback to basic Auth details (Fixes your error!)
            val message = Message(
                senderFirstName = authUser.email?.split("@")?.get(0) ?: "User", // Use email prefix as name
                senderId = authUser.email ?: "",
                text = text
            )
            sendMessageToRepository(message)
        } else {
            // Scenario C: Truly logged out
            android.util.Log.e("ChatDebug", "ERROR: User is completely logged out. Cannot send.")
        }
    }

    private fun sendMessageToRepository(message: Message) {
        viewModelScope.launch {
            val roomId = _roomId.value.toString()
            android.util.Log.d("ChatDebug", "Attempting to send to Room: $roomId")

            when (messageRepository.sendMessage(roomId, message)) {
                is Result.Success -> android.util.Log.d("ChatDebug", "Message Sent Successfully!")
                is Result.Error -> android.util.Log.e("ChatDebug", "Failed to send message")
            }
        }
    }

    private fun loadMessages() {
        viewModelScope.launch {
            if (_roomId.value != null) {
                messageRepository.getChatMessages(_roomId.value.toString())
                    .collect { _messages.value = it }
            }
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is Result.Success -> _currentUser.value = result.data
                is Result.Error -> { }
            }
        }
    }
}