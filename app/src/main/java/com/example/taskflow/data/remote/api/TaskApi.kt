package com.example.taskflow.data.remote.api

import com.example.taskflow.data.remote.dto.TaskDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TaskApi {

    @GET("tasks")
    suspend fun getTasks():
            Response<List<TaskDto>>

    @POST("tasks")
    suspend fun syncTask(
        @Body task: TaskDto
    ): Response<TaskDto>
}