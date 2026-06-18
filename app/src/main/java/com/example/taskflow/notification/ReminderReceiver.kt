package com.example.taskflow.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {


    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        android.util.Log.d(
            "REMINDER",
            "Receiver called"
        )

        val taskId =
            intent.getStringExtra("task_id") ?: ""

        val title =
            intent.getStringExtra(
                "task_title"
            ) ?: "Task Reminder"

        val description =
            intent.getStringExtra(
                "task_description"
            ) ?: "You have a task to do."

        val helper =
            NotificationHelper(context)

        helper.createNotificationChannel()

        helper.showTaskReminder(
            taskId = taskId.hashCode(),
            title = title,
            description = description
        )
    }
}