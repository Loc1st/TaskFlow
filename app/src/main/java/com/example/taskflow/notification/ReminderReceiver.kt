package com.example.taskflow.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val taskId =
            intent.getIntExtra(
                "task_id",
                -1
            )

        val title =
            intent.getStringExtra(
                "task_title"
            ) ?: "Task Reminder"

        val description =
            intent.getStringExtra(
                "task_description"
            ) ?: "You have a task to do."

        val helper =
            NotificationHelper(
                context
            )

        helper.createNotificationChannel()

        helper.showTaskReminder(
            taskId = taskId,
            title = title,
            description = description
        )
    }
}