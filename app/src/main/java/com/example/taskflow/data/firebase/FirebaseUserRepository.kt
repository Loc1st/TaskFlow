package com.example.taskflow.data.firebase

import com.example.taskflow.data.firebase.model.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private val auth = FirebaseAuth.getInstance()

    fun createUser(
        username: String,
        email: String,
        phone: String = "",
        avatarUrl: String = "",
        onResult: (Boolean) -> Unit
    ) {

        val uid = auth.currentUser?.uid ?: return

        val user = FirebaseUser(
            uid = uid,
            username = username,
            email = email,
            phone = phone,
            avatarUrl = avatarUrl
        )

        firestore
            .collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun getCurrentUser(
        onResult: (FirebaseUser?) -> Unit
    ) {

        val uid = auth.currentUser?.uid ?: return

        firestore
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {

                onResult(
                    it.toObject(FirebaseUser::class.java)
                )

            }
            .addOnFailureListener {

                onResult(null)

            }
    }

    fun updateProfile(
        username: String,
        phone: String,
        avatarUrl: String,
        onResult: (Boolean) -> Unit
    ) {

        val uid = auth.currentUser?.uid ?: return

        val updateMap = hashMapOf<String, Any>(
            "username" to username,
            "phone" to phone,
            "avatarUrl" to avatarUrl
        )

        firestore
            .collection("users")
            .document(uid)
            .update(updateMap)
            .addOnSuccessListener {

                onResult(true)

            }
            .addOnFailureListener {

                onResult(false)

            }

    }

}