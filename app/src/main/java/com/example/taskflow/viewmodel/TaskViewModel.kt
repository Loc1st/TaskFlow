package com.example.taskflow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.local.entity.TaskEntity
import com.example.taskflow.data.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    fun addTask(
        task: TaskEntity,
        onResult: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val id =
                taskRepository.addTask(task)
            onResult(id)
        }
    }

    fun updateTask(
        task: TaskEntity
    ) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(
        task: TaskEntity
    ) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun getTasksByUser(
        userId: Int
    ): LiveData<List<TaskEntity>> {
        return taskRepository.getTasksByUser(
            userId
        )
    }

    fun getTasksByDate(
        userId: Int,
        date: String
    ): LiveData<List<TaskEntity>> {
        return taskRepository.getTasksByDate(
            userId,
            date
        )
    }

    fun getPendingTasks(
        userId: Int
    ): LiveData<List<TaskEntity>> {
        return taskRepository.getPendingTasks(
            userId
        )
    }

    fun getCompletedTasks(
        userId: Int
    ): LiveData<List<TaskEntity>> {
        return taskRepository.getCompletedTasks(
            userId
        )
    }

    fun getTasksByPriority(
        userId: Int,
        priority: String
    ): LiveData<List<TaskEntity>> {

        return taskRepository
            .getTasksByPriority(
                userId,
                priority
            )
    }

    fun markTaskCompleted(
        task: TaskEntity
    ) {
        viewModelScope.launch {
            taskRepository.markTaskCompleted(
                task
            )
        }
    }

    fun getTaskById(
        taskId: Int,
        onResult: (TaskEntity?) -> Unit
    ) {
        viewModelScope.launch {
            val task =
                taskRepository.getTaskById(
                    taskId
                )
            onResult(task)
        }
    }
}