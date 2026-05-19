package com.example.taskflow.data.repository

import androidx.lifecycle.LiveData
import com.example.taskflow.data.local.dao.TaskDao
import com.example.taskflow.data.local.entity.TaskEntity

class TaskRepository(
    private val taskDao: TaskDao
) {

    // Add task
    suspend fun addTask(
        task: TaskEntity
    ): Long {
        return taskDao.insertTask(task)
    }

    // Update task
    suspend fun updateTask(
        task: TaskEntity
    ) {
        taskDao.updateTask(task)
    }

    // Delete task
    suspend fun deleteTask(
        task: TaskEntity
    ) {
        taskDao.deleteTask(task)
    }

    // All task theo user
    fun getTasksByUser(
        userId: Int
    ): LiveData<List<TaskEntity>> {
        return taskDao.getTasksByUser(
            userId
        )
    }

    // Calendar task theo ngày
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

    // Mark complete
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

    // Pending sync
    suspend fun getPendingSyncTasks():
            List<TaskEntity> {
        return taskDao.getPendingSyncTasks()
    }
}