package com.example.taskflow.data.repository

import com.example.taskflow.data.local.dao.UserDao
import com.example.taskflow.data.local.entity.UserEntity
import com.example.taskflow.data.remote.dto.LoginRequest
import com.example.taskflow.data.remote.retrofit.RetrofitClient

class UserRepository(
    private val userDao: UserDao
) {

    // Register local
    suspend fun registerUser(
        user: UserEntity
    ): Long {
        return userDao.insertUser(user)
    }

    // Login local trước (offline-first)
    suspend fun login(
        email: String,
        password: String
    ): UserEntity? {
        return userDao.login(
            email,
            password
        )
    }

    // Login API (online)
    suspend fun loginFromApi(
        email: String,
        password: String
    ): UserEntity? {
        return try {

            val response =
                RetrofitClient.authApi.login(
                    LoginRequest(
                        email,
                        password
                    )
                )

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }

        } catch (e: Exception) {
            null
        }
    }

    // Check email local
    suspend fun getUserByEmail(
        email: String
    ): UserEntity? {
        return userDao.getUserByEmail(
            email
        )
    }

    // Current user
    suspend fun getUserById(
        userId: Int
    ): UserEntity? {
        return userDao.getUserById(
            userId
        )
    }
    suspend fun updateAvatar(userId: Int, path: String) {
        userDao.updateAvatar(userId, path)
    }

    suspend fun updatePassword(userId: Int, newPassword: String){
        userDao.updatePassword(userId, newPassword)
    }
}