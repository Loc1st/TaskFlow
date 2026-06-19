package com.example.taskflow.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.taskflow.data.firebase.model.FirebaseUser

class FirebaseAuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun register(

        username: String,

        email: String,

        password: String,

        onResult: (Boolean, String?) -> Unit

    ) {

        auth.createUserWithEmailAndPassword(
            email,
            password
        )
            .addOnSuccessListener {

                val firebaseUser = auth.currentUser!!

                android.util.Log.d("REGISTER", "UID = ${firebaseUser.uid}")

                val user = FirebaseUser(

                    uid = firebaseUser.uid,

                    username = username,

                    email = email,

                    phone = "",

                    avatarUrl = ""

                )

                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(firebaseUser.uid)
                    .set(user)
                    .addOnSuccessListener {

                        android.util.Log.d("REGISTER", "User saved to Firestore")

                        onResult(true, null)

                    }
                    .addOnFailureListener {

                        android.util.Log.e("REGISTER", "Firestore error", it)

                        onResult(false, it.localizedMessage)

                    }

            }
            .addOnFailureListener {

                onResult(false, it.localizedMessage)

            }

    }

    fun login(

        email: String,

        password: String,

        onResult: (Boolean, String?) -> Unit

    ) {

        auth.signInWithEmailAndPassword(
            email,
            password
        )
            .addOnSuccessListener {

                onResult(
                    true,
                    null
                )

            }
            .addOnFailureListener {

                onResult(
                    false,
                    it.localizedMessage
                )

            }

    }

    fun logout() {

        auth.signOut()

    }

    fun isLoggedIn(): Boolean {

        return auth.currentUser != null

    }

    fun getCurrentUid(): String? {

        return auth.currentUser?.uid

    }

    fun getCurrentEmail(): String? {

        return auth.currentUser?.email

    }

}