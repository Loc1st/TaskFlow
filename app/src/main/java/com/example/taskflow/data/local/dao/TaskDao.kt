package com.example.taskflow.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskflow.data.local.entity.TaskEntity

@Dao
interface TaskDao {

    // Add task
    @Insert
    suspend fun insertTask(task: TaskEntity): Long

    // Update task
    @Update
    suspend fun updateTask(task: TaskEntity)

    // Delete task
    @Delete
    suspend fun deleteTask(task: TaskEntity)

    // All task theo user
    @Query("""
        SELECT * FROM tasks
        WHERE userId = :userId
        ORDER BY startDate, startTime
    """)
    fun getTasksByUser(
        userId: Int
    ): LiveData<List<TaskEntity>>

    // Task theo ngày (Calendar)
    @Query("""
        SELECT * FROM tasks
        WHERE userId = :userId
        AND startDate = :date
        ORDER BY startTime
    """)
    fun getTasksByDate(
        userId: Int,
        date: String
    ): LiveData<List<TaskEntity>>

    // Task pending
    @Query("""
        SELECT * FROM tasks
        WHERE userId = :userId
        AND isCompleted = 0
    """)
    fun getPendingTasks(
        userId: Int
    ): LiveData<List<TaskEntity>>

    // Task completed
    @Query("""
        SELECT * FROM tasks
        WHERE userId = :userId
        AND isCompleted = 1
    """)
    fun getCompletedTasks(
        userId: Int
    ): LiveData<List<TaskEntity>>

    // Task cần sync
    @Query("""
        SELECT * FROM tasks
        WHERE syncPending = 1
    """)
    suspend fun getPendingSyncTasks():
            List<TaskEntity>

    // Task detail
    @Query("""
        SELECT * FROM tasks
        WHERE id = :taskId
        LIMIT 1
    """)
    suspend fun getTaskById(
        taskId: Int
    ): TaskEntity?

    @Query("""
    SELECT * FROM tasks
    WHERE userId = :userId
    AND LOWER(priority) = LOWER(:priority)
    ORDER BY startDate, startTime
""")
    fun getTasksByPriority(
        userId: Int,
        priority: String
    ): LiveData<List<TaskEntity>>
}