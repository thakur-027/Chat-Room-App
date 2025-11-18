package com.example.chatroomapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (email.isEmpty() || pass.isEmpty()) {
            onError("Please fill in all fields")
            return
        }
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSuccess()
                        } else {
                            onError(task.exception?.message ?: "Login failed")
                        }
                    }
            } catch (e: Exception) {
                onError(e.message ?: "An error occurred")
            }
        }
    }
}