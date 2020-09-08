package com.masrooraijaz.fireinsta.ui.auth

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.masrooraijaz.fireinsta.repository.auth.AuthRepository
import com.masrooraijaz.fireinsta.util.DataOrException
import org.koin.core.KoinComponent
import org.koin.core.inject

import java.lang.Exception

class AuthViewModel : ViewModel(), KoinComponent {


    private val authRepository by inject<AuthRepository>()

    var user = MediatorLiveData<DataOrException<FirebaseUser, Exception>>()
    var errorLiveData = MutableLiveData<String>()

    fun createUser(
        displayName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {

        when {
            displayName.isBlank() -> errorLiveData.value = "Please enter your name"
            email.isBlank() -> errorLiveData.value = "Please enter your email address"
            password.isBlank() -> errorLiveData.value = "Please enter your password"
            confirmPassword.isBlank() -> errorLiveData.value = "Please confirm your password"
            password != confirmPassword -> errorLiveData.value = "Passwords do not match"
            else -> {
                user.value = DataOrException(loading = true)
                val source = authRepository.createUser(displayName, email, password)
                user.addSource(source, Observer {
                    user.value = it
                    user.removeSource(source)
                })
            }
        }

    }

    fun authenticateUser(email: String, password: String) {
        when {
            email.isBlank() -> errorLiveData.value = "Please enter your email address"
            password.isBlank() -> errorLiveData.value = "Please enter your password"
            else -> {
                user.value = DataOrException(loading = true)
                val source = authRepository.loginUser(email, password)
                user.addSource(source, Observer {
                    user.value = it
                    user.removeSource(source)
                })
            }
        }
    }

    fun isAuthenticated() = authRepository.isAuthenticated()
}