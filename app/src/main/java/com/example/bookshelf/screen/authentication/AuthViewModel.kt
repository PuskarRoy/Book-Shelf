package com.example.bookshelf.screen.authentication

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.model.MUser
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth = Firebase.auth
    val loading = mutableStateOf(false)

    fun loginUser(email: String, password: String, onDone: () -> Unit, onFail: () -> Unit) {
        loading.value = true
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                  // if(auth.currentUser!=null) loading.value = false
                    onDone()
                }.addOnFailureListener {
                    loading.value = false
                    onFail()
                }
            } catch (e: Exception) {
                loading.value = false
                Log.d("LogInUser", e.toString())
            }
        }

    }

    fun createUser(email: String, password: String, onDone: () -> Unit) {
        loading.value = true

        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                    val displayName = it.user?.email?.split('@')?.get(0)
                    if (displayName != null) {
                        storeInUserDatabase(displayName)
                    }

                    loading.value = false
                    onDone()
                }
            } catch (e: Exception) {
                loading.value = false
                Log.d("CreateUser", e.message.toString())
            }

        }

    }


    private fun storeInUserDatabase(name: String) {
        val db = Firebase.firestore
        val userId = auth.currentUser?.uid

        val data = MUser(userId = userId.toString(), avatarUrl = "", displayName = name).toMap()
        db.collection("Users").add(data)

    }


}