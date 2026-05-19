package com.example.taskflow.data.remote.retrofit

import com.example.taskflow.data.remote.api.AuthApi
import com.example.taskflow.data.remote.api.TaskApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Tạm base URL demo
    private const val BASE_URL =
        "https://example.com/api/"

    private val logging =
        HttpLoggingInterceptor().apply {
            level =
                HttpLoggingInterceptor.Level.BODY
        }

    private val client =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

    val authApi: AuthApi =
        retrofit.create(
            AuthApi::class.java
        )

    val taskApi: TaskApi =
        retrofit.create(
            TaskApi::class.java
        )
}