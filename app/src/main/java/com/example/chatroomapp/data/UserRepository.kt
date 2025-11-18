package com.example.chatroomapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun signUp(email: String, pass: String, firstName: String, lastName: String): Result<Boolean> {
        return try {
            auth.createUserWithEmailAndPassword(email, pass).await()
            val user = User(firstName, lastName, email)
            saveUserToFirestore(user)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // --- NEW LOGIN FUNCTION ---
    suspend fun login(email: String, pass: String): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Add inside UserRepository class:
    suspend fun getCurrentUser(): Result<User> = try {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            val document = firestore.collection("users").document(uid).get().await()
            val user = document.toObject(User::class.java)!!
            Result.Success(user)
        } else {
            Result.Error(Exception("User not logged in"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.email).set(user).await()
    }
}