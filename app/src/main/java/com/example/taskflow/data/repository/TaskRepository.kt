package com.example.taskflow.data.repository

import androidx.lifecycle.LiveData
import com.example.taskflow.data.local.dao.TaskDao
import com.example.taskflow.data.local.entity.TaskEntity
import com.example.taskflow.data.remote.dto.TaskDto
import com.example.taskflow.data.remote.retrofit.RetrofitClient

class TaskRepository(
    private val taskDao: TaskDao
) {

    // Add local
    suspend fun addTask(
        task: TaskEntity
    ): Long {
        return taskDao.insertTask(task)
    }

    // Update
    suspend fun updateTask(
        task: TaskEntity
    ) {
        taskDao.updateTask(task)
    }

    // Delete
    suspend fun deleteTask(
        task: TaskEntity
    ) {
        taskDao.deleteTask(task)
    }

    // All by user
    fun getTasksByUser(
        userId: Int
    ): LiveData<List<TaskEntity>> {
        return taskDao.getTasksByUser(
            userId
        )
    }

    // Calendar
    fun getTasksByDate(
        userId: Int,
        date: String
    ): LiveData<List<TaskEntity>> {
        return taskDao.getTasksByDate(
            userId,
            date
        )
    }

    // Pending
    fun getPendingTasks(
        userId: Int
    ): LiveData<List<TaskEntity>> {
        return taskDao.getPendingTasks(
            userId
        )
    }

    // Completed
    fun getCompletedTasks(
        userId: Int
    ): LiveData<List<TaskEntity>> {
        return taskDao.getCompletedTasks(
            userId
        )
    }

    // Detail
    suspend fun getTaskById(
        taskId: Int
    ): TaskEntity? {
        return taskDao.getTaskById(
            taskId
        )
    }

    // Complete task
    suspend fun markTaskCompleted(
        task: TaskEntity
    ) {
        taskDao.updateTask(
            task.copy(
                isCompleted = true,
                reminderEnabled = false,
                syncPending = true
            )
        )
    }

    // Pending sync local
    suspend fun getPendingSyncTasks():
            List<TaskEntity> {
        return taskDao.getPendingSyncTasks()
    }

    // Push task lên server
    suspend fun syncTaskToApi(
        task: TaskEntity
    ): Boolean {
        return try {

            val dto = TaskDto(
                id = task.id,
                userId = task.userId,
                title = task.title,
                description = task.description,
                startDate = task.startDate,
                startTime = task.startTime,
                endDate = task.endDate,
                endTime = task.endTime,
                priority = task.priority,
                category = task.category,
                reminderEnabled = task.reminderEnabled,
                isCompleted = task.isCompleted
            )

            val response =
                RetrofitClient.taskApi.syncTask(dto)

            response.isSuccessful

        } catch (e: Exception) {
            false
        }
    }

    // Fetch task từ server
    suspend fun fetchTasksFromApi():
            List<TaskDto>? {
        return try {

            val response =
                RetrofitClient.taskApi.getTasks()

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }

        } catch (e: Exception) {
            null
        }
    }
}