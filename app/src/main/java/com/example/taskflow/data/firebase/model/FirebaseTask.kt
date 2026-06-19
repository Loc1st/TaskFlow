package com.example.taskflow.data.firebase.model

data class FirebaseTask(

    val id: String = "",

    val userId: String = "",

    val title: String = "",

    val description: String = "",

    val startDate: String = "",

    val startTime: String = "",

    val endDate: String = "",

    val endTime: String = "",

    val priority: String = "",

    val category: String = "",

    val reminderEnabled: Boolean = false,

    val completed: Boolean = false,

    val createdAt: Long = System.currentTimeMillis()

)