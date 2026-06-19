package com.example.taskflow.data.firebase

import com.example.taskflow.data.firebase.model.FirebaseTask
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class FirebaseTaskRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private val auth = FirebaseAuth.getInstance()

    fun addTask(
        task: FirebaseTask,
        onResult: (Boolean, FirebaseTask?) -> Unit
    ) {

        val uid = auth.currentUser?.uid ?: run {

            onResult(false, null)

            return
        }

        val doc = firestore
            .collection("tasks")
            .document()

        val firebaseTask = task.copy(
            id = doc.id,
            userId = uid
        )

        doc.set(firebaseTask)
            .addOnSuccessListener {

                onResult(
                    true,
                    firebaseTask
                )

            }
            .addOnFailureListener {

                onResult(
                    false,
                    null
                )

            }

    }

    fun updateTask(
        task: FirebaseTask,
        onResult: (Boolean) -> Unit
    ) {

        firestore
            .collection("tasks")
            .document(task.id)
            .set(task)
            .addOnSuccessListener {

                onResult(true)

            }
            .addOnFailureListener {

                onResult(false)

            }

    }

    fun deleteTask(
        taskId: String,
        onResult: (Boolean) -> Unit
    ) {

        firestore
            .collection("tasks")
            .document(taskId)
            .delete()
            .addOnSuccessListener {

                onResult(true)

            }
            .addOnFailureListener {

                onResult(false)

            }

    }

    fun listenTasks(
        onChanged: (List<FirebaseTask>) -> Unit
    ): ListenerRegistration? {

        val uid = auth.currentUser?.uid ?: return null

        return firestore
            .collection("tasks")
            .whereEqualTo(
                "userId",
                uid
            )
            .addSnapshotListener { value, _ ->

                if (value == null) return@addSnapshotListener

                onChanged(
                    value.toObjects(
                        FirebaseTask::class.java
                    )
                )

            }

    }

}