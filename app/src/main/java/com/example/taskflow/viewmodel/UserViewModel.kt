package com.example.taskflow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskflow.data.firebase.FirebaseUserRepository
import com.example.taskflow.data.firebase.model.FirebaseUser
import com.google.firebase.auth.FirebaseAuth

class UserViewModel : ViewModel() {

    private val repository = FirebaseUserRepository()

    private val _currentUser =
        MutableLiveData<FirebaseUser?>()

    val currentUser: LiveData<FirebaseUser?> =
        _currentUser

    private var loadedUid: String? = null

    fun loadCurrentUser() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid == null) {

            _currentUser.value = null
            loadedUid = null
            return

        }

        if (loadedUid == uid && _currentUser.value != null) {

            return

        }

        repository.getCurrentUser {

            loadedUid = uid

            _currentUser.postValue(it)

        }

    }

    fun refreshUser() {

        loadedUid = null

        loadCurrentUser()

    }

    fun clearUser() {

        loadedUid = null

        _currentUser.value = null

    }

}