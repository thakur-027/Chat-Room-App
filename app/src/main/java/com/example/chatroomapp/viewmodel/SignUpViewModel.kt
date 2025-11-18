package com.example.chatroomapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatroomapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun signUp(email: String, pass: String, firstName: String, lastName: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (email.isEmpty() || pass.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            onError("Please fill in all fields")
            return
        }
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = User(firstName, lastName, email)
                            val uid = auth.currentUser?.uid
                            if (uid != null) {
                                db.collection("users").document(uid).set(user)
                                    .addOnSuccessListener { onSuccess() }
                                    .addOnFailureListener { onError(it.message ?: "Error") }
                            }
                        } else {
                            onError(task.exception?.message ?: "Sign up failed")
                        }
                    }
            } catch (e: Exception) {
                onError(e.message ?: "An error occurred")
            }
        }
    }
}