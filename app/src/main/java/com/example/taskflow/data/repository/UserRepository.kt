package com.example.taskflow.data.repository

import com.example.taskflow.data.local.dao.UserDao
import com.example.taskflow.data.local.entity.UserEntity

class UserRepository(
    private val userDao: UserDao
) {

    // Register
    suspend fun registerUser(
        user: UserEntity
    ): Long {
        return userDao.insertUser(user)
    }

    // Login
    suspend fun login(
        email: String,
        password: String
    ): UserEntity? {
        return userDao.login(
            email,
            password
        )
    }

    // Check email
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
}