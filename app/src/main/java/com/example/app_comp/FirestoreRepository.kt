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

    fun postComplaint(complaint: Complaint): Flow<Result<Unit>> {
        return callbackFlow {
            try {
                db.collection("complaints")
                    .add(complaint)
                    .addOnSuccessListener {
                        Log.d(DEBUGGING, "Complaint added successfully")
                        trySend(Result.Success(Unit)).isSuccess
                    }
                    .addOnFailureListener {
                        Log.d(DEBUGGING, "Error adding complaint: ${it.message}")
                        trySend(Result.Error(it.message!!)).isSuccess
                    }
            } catch (e: Exception) {
                Log.d(DEBUGGING, "Error adding complaint: ${e.message}")
                trySend(Result.Error(e.message!!)).isSuccess
            }
            awaitClose {
                Log.d(DEBUGGING, "Cancelling post complaint listener")
            }
        }
    }



}