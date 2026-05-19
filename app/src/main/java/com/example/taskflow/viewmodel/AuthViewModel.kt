package com.example.taskflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.local.entity.UserEntity
import com.example.taskflow.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    fun registerUser(
        user: UserEntity,
        onResult: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val result =
                userRepository.registerUser(user)
            onResult(result)
        }
    }

    fun login(
        email: String,
        password: String,
        onResult: (UserEntity?) -> Unit
    ) {
        viewModelScope.launch {
            val user =
                userRepository.login(
                    email,
                    password
                )
            onResult(user)
        }
    }

    fun checkEmailExists(
        email: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val user =
                userRepository.getUserByEmail(
                    email
                )
            onResult(user != null)
        }
    }
}