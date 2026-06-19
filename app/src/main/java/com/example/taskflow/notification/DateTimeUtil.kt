package com.example.taskflow.notification

import java.text.SimpleDateFormat
import java.util.Locale

object DateTimeUtil {

    fun toMillis(
        date: String,
        time: String
    ): Long {

        return try {

            val format = SimpleDateFormat(
                "yyyy-MM-dd HH:mm",
                Locale.getDefault()
            )

            format.parse("$date $time")?.time ?: 0L

        } catch (e: Exception) {

            0L

        }

    }

}