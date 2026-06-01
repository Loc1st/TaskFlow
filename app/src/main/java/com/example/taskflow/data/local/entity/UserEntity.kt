package com.example.taskflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val username: String,

    val email: String,

    val password: String,

    val avatarPath: String? = null
    //val avatarUrl: String? = null


)