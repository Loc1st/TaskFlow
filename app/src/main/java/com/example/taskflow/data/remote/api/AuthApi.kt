package com.example.taskflow.data.remote.api

import com.example.taskflow.data.local.entity.UserEntity
import com.example.taskflow.data.remote.dto.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<UserEntity>
}