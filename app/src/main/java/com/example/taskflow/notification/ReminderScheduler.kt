package com.example.taskflow.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class ReminderScheduler(
    private val context: Context
) {

    fun scheduleReminder(
        taskId: String,
        title: String,
        description: String,
        triggerTimeMillis: Long
    ) {
        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        val intent = Intent(
            context,
            ReminderReceiver::class.java
        ).apply {
            putExtra("task_id", taskId)
            putExtra("task_title", title)
            putExtra(
                "task_description",
                description
            )
        }

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                taskId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        or PendingIntent.FLAG_IMMUTABLE
            )


        android.util.Log.d(
            "REMINDER",
            "Alarm: $triggerTimeMillis"
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {

            if (alarmManager.canScheduleExactAlarms()) {

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
                    pendingIntent
                )

            } else {

                android.util.Log.e(
                    "REMINDER",
                    "Exact alarm permission denied"
                )

            }

        } else {

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )

        }

    }

    fun cancelReminder(
        taskId: String
    ) {
        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        val intent = Intent(
            context,
            ReminderReceiver::class.java
        )

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                taskId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        or PendingIntent.FLAG_IMMUTABLE
            )

        alarmManager.cancel(
            pendingIntent
        )
    }
}