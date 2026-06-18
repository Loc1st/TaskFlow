package com.example.taskflow.data.repository

import com.example.taskflow.data.firebase.FirebaseTaskRepository
import com.example.taskflow.data.firebase.model.FirebaseTask
import com.google.firebase.firestore.ListenerRegistration

class TaskRepository {

    private val firebaseRepository =
        FirebaseTaskRepository()

    fun addTask(
        task: FirebaseTask,
        onResult: (Boolean, FirebaseTask?) -> Unit
    ) {

        firebaseRepository.addTask(
            task,
            onResult
        )

    }

    fun updateTask(
        task: FirebaseTask,
        onResult: (Boolean) -> Unit
    ) {

        firebaseRepository.updateTask(
            task,
            onResult
        )

    }

    fun deleteTask(
        taskId: String,
        onResult: (Boolean) -> Unit
    ) {

        firebaseRepository.deleteTask(
            taskId,
            onResult
        )

    }

    fun listenTasks(
        onChanged: (List<FirebaseTask>) -> Unit
    ): ListenerRegistration? {

        return firebaseRepository.listenTasks(
            onChanged
        )

    }

}