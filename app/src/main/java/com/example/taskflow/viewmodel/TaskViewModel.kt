package com.example.taskflow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.taskflow.data.firebase.model.FirebaseTask
import com.example.taskflow.data.repository.TaskRepository
import com.google.firebase.firestore.ListenerRegistration

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private var listener:
            ListenerRegistration? = null

    fun addTask(
        task: FirebaseTask,
        onResult: (Boolean, FirebaseTask?) -> Unit
    ) {

        repository.addTask(
            task,
            onResult
        )

    }

    fun updateTask(
        task: FirebaseTask,
        onResult: (Boolean) -> Unit
    ) {

        repository.updateTask(
            task,
            onResult
        )

    }

    fun deleteTask(
        taskId: String,
        onResult: (Boolean) -> Unit
    ) {

        repository.deleteTask(
            taskId,
            onResult
        )

    }

    fun listenTasks(

        onChanged:
            (List<FirebaseTask>) -> Unit

    ) {

        listener?.remove()

        listener =
            repository.listenTasks(
                onChanged
            )

    }

    override fun onCleared() {

        listener?.remove()

        super.onCleared()

    }

    fun stopListening() {

        listener?.remove()

        listener = null

    }

}