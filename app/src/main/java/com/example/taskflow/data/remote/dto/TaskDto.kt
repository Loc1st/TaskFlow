package com.example.taskflow.data.remote.dto

data class TaskDto(
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val startDate: String,
    val startTime: String,
    val endDate: String,
    val endTime: String,
    val priority: String,
    val category: String,
    val reminderEnabled: Boolean,
    val isCompleted: Boolean
)