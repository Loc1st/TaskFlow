package com.example.taskflow.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskflow.data.local.dao.TaskDao
import com.example.taskflow.data.local.dao.UserDao
import com.example.taskflow.data.local.entity.TaskEntity
import com.example.taskflow.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TaskEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun taskDao(): TaskDao
}