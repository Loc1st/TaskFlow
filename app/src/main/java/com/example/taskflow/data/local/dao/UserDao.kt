package com.example.taskflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.taskflow.data.local.entity.UserEntity

@Dao
interface UserDao {

    // Đăng ký account
    @Insert
    suspend fun insertUser(user: UserEntity): Long

    // Login bằng email + password
    @Query("""
        SELECT * FROM users
        WHERE email = :email
        AND password = :password
        LIMIT 1
    """)
    suspend fun login(
        email: String,
        password: String
    ): UserEntity?

    // Lấy user theo id (session)
    @Query("""
        SELECT * FROM users
        WHERE id = :userId
        LIMIT 1
    """)
    suspend fun getUserById(
        userId: Int
    ): UserEntity?

    // Check email đã tồn tại chưa
    @Query("""
        SELECT * FROM users
        WHERE email = :email
        LIMIT 1
    """)
    suspend fun getUserByEmail(
        email: String
    ): UserEntity?

    @Query("""
        UPDATE users 
        SET avatarPath = :path 
        WHERE id = :userId
    """)
    suspend fun updateAvatar(
        userId: Int,
        path: String
    )

    // đổi mk
    @Query("""
        UPDATE users
        SET password = :newPassword
        WHERE id = :userId
    """)
    suspend fun updatePassword(
        userId: Int,
        newPassword: String,
    )

    @Query("""
        UPDATE users 
        SET username = :username, email = :email, phone = :phone 
        WHERE id = :userId
    """)
    suspend fun updateProfile(
        userId: Int,
        username: String,
        email: String,
        phone: String
    )
}