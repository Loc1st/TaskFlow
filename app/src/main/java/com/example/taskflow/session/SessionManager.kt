package com.example.taskflow.session

import android.content.Context

class SessionManager(
    context: Context
) {

    private val sharedPreferences =
        context.getSharedPreferences(
            "taskflow_session",
            Context.MODE_PRIVATE
        )

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    // Save login
    fun saveLoginSession(
        userId: Int
    ) {
        sharedPreferences.edit()
            .putInt(KEY_USER_ID, userId)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }

    // Get current user id
    fun getCurrentUserId(): Int {
        return sharedPreferences.getInt(
            KEY_USER_ID,
            -1
        )
    }

    // Check login
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(
            KEY_IS_LOGGED_IN,
            false
        )
    }

    // Logout
    fun logout() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}