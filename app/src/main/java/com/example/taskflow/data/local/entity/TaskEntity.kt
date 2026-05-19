package com.example.taskflow.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: Int,

    val title: String,

    val description: String,

    val startDate: String,
    val startTime: String,

    val endDate: String,
    val endTime: String,

    val priority: String,          // High / Medium / Low

    val category: String,          // Study / Work / Personal

    val reminderEnabled: Boolean = false,

    val isCompleted: Boolean = false,

    val syncPending: Boolean = false
)