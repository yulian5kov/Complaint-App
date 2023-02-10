package com.example.app_comp

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreRepository {

    fun addUser(user: User, password: String): Flow<Result<User>> {
        return callbackFlow {
            mAuth
                .createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener { it ->
                    user.id = it.user?.uid!!
                    db.collection("users").document(user.id)
                        .set(user)
                        .addOnSuccessListener {
                            trySend(Result.Success(user)).isSuccess
                        }
                        .addOnFailureListener {
                            trySend(Result.Error(it.message!!)).isSuccess
                        }
                }
                .addOnFailureListener {
                    trySend(Result.Error(it.message!!)).isSuccess
                }
            awaitClose {
                Log.d(DEBUGGING, "Cancelling posts listener")
            }
        }
    }

    fun loginUser(email: String, password: String): Flow<Result<User>> {
        return callbackFlow {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { it ->
                    db.collection("users").document(it.user?.uid!!)
                        .get()
                        .addOnSuccessListener {
                            if(it.exists()) {
                                val user = it.toObject(User::class.java)
                                trySend(Result.Success(user!!)).isSuccess
                            }else{
                                trySend(Result.Failed("User not found", "swag")).isSuccess
                            }
                        }
                        .addOnFailureListener {
                            trySend(Result.Error(it.message!!)).isSuccess
                        }
            }
                .addOnFailureListener {
                    trySend(Result.Error(it.message!!)).isSuccess
                }
            awaitClose {
                Log.d(DEBUGGING, "Cancelling login listener")
            }
        }
    }

    fun postComplaint(text: String, images: List<Uri>, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val complaintRef = db.collection("complaints").document()
        val complaint = hashMapOf(
            "text" to text,
            "images" to images.map { it.toString() }
        )
        complaintRef.set(complaint)
            .addOnSuccessListener {
                onSuccess("Complaint uploaded successfully with ID: ${complaintRef.id}")
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}