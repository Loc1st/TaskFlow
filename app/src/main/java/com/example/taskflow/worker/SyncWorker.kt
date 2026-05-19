package com.example.taskflow.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskflow.data.local.db.DatabaseProvider

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(
    context,
    params
) {

    override suspend fun doWork(): Result {
        return try {

            val database =
                DatabaseProvider.getDatabase(
                    applicationContext
                )

            val taskDao =
                database.taskDao()

            val pendingTasks =
                taskDao.getPendingSyncTasks()

            // Sau này:
            // gọi Retrofit API sync server

            for (task in pendingTasks) {
                taskDao.updateTask(
                    task.copy(
                        syncPending = false
                    )
                )
            }

            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }
}