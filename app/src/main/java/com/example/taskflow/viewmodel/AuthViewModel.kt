package com.example.taskflow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.taskflow.data.firebase.FirebaseAuthRepository
import com.example.taskflow.data.firebase.FirebaseUserRepository

class AuthViewModel : ViewModel() {

    private val authRepository =
        FirebaseAuthRepository()

    private val userRepository =
        FirebaseUserRepository()

    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {

        authRepository.login(
            email,
            password
        ) { success, message ->

            onResult(
                success,
                message
            )

        }

    }

    fun register(
        username: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {

        authRepository.register(
            username = username,
            email = email,
            password = password
        ) { success, message ->

            if (!success) {

                onResult(
                    false,
                    message
                )

                return@register
            }

            onResult(
                success,
                message
            )

        }

    }

    fun logout() {

        authRepository.logout()

    }

    fun isLoggedIn(): Boolean {

        return authRepository.isLoggedIn()

    }

}