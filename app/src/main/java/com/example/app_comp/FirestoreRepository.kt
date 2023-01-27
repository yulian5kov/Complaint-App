package com.example.app_comp

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreRepository {

    fun addUser(user: User, password: String): Flow<Event<Unit>> {
        return callbackFlow {
            mAuth
                .createUserWithEmailAndPassword(user.email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i(DEBUGGING, "createUserWithEmail:success")
                        val firebaseUser = task.result?.user
                        firebaseUser?.let {
                            db
                                .collection("users")
                                .document(it.uid)
                                .set(user)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.i(DEBUGGING, "User added successfully")
                                        trySend(Event.Success(Unit)).isSuccess
                                    } else {
                                        trySend(Event.Error(task.exception?.message.toString())).isSuccess
                                    }
                                }
                        }
                    } else {
                        trySend(Event.Error(task.exception?.message.toString())).isSuccess
                    }
                }
            awaitClose {
                Log.d(DEBUGGING, "Cancelling posts listener")
            }
        }
    }

    fun loginUser(email: String, password: String): Flow<Event<Unit>> {
        return callbackFlow {
            mAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i(DEBUGGING, "signInWithEmail:success")
                        val firebaseUser = task.result?.user
                        firebaseUser?.let {
                            trySend(Event.Success(Unit)).isSuccess
                        }
                    } else {
                        trySend(Event.Error(task.exception?.message.toString())).isSuccess
                    }
                }
            awaitClose {
                Log.d(DEBUGGING, "Cancelling posts listener")
            }
        }
    }
}