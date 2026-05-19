package com.example.taskflow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.taskflow.session.SessionManager

class SessionViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    fun saveLogin(
        userId: Int
    ) {
        sessionManager.saveLoginSession(
            userId
        )
    }

    fun getCurrentUserId(): Int {
        return sessionManager.getCurrentUserId()
    }

    fun isLoggedIn(): Boolean {
        return sessionManager.isLoggedIn()
    }

    fun logout() {
        sessionManager.logout()
    }
}