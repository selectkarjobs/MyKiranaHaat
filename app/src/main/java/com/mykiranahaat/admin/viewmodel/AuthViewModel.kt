package com.mykiranahaat.admin.viewmodel

import android.app.Activity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.concurrent.TimeUnit

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    var mobileNumber = mutableStateOf("")
    var otp = mutableStateOf("")
    var verificationId = mutableStateOf("")
    var authState = mutableStateOf<AuthState>(AuthState.Idle)
    private val auth = Firebase.auth

    fun sendOtp(phoneNumber: String, activity: Activity, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        viewModelScope.launch {
            authState.value = AuthState.Loading
            val formattedNumber = if (phoneNumber.startsWith("+")) phoneNumber else "+91$phoneNumber"
            PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(formattedNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(callbacks)
                    .build()
            )
        }
    }

    fun verifyOtp(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            authState.value = AuthState.Loading
            try {
                auth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        authState.value = AuthState.Success
                    } else {
                        authState.value = AuthState.Error(task.exception?.message ?: "Authentication failed")
                    }
                }
            } catch (e: Exception) {
                authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}