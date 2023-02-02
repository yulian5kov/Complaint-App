package com.example.app_comp

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

    fun postComplaint(complaint: String): Flow<Result<Unit>> {
        return callbackFlow {
            db.collection("complaints")
                .add(complaint)
                .addOnSuccessListener {
                    trySend(Result.Success(Unit))
                }
                .addOnFailureListener {
                    trySend(Result.Error(it.message!!))
                }
            awaitClose {
                Log.d(DEBUGGING, "Cancelling post complaint listener")
            }
        }
    }

    fun getComplaints(userId: String): Flow<Result<List<Complaint>>> {
        return callbackFlow {
            db.collection("complaints")
                .whereEqualTo("userId", userId)
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    //GlobalScope.launch {}

                    if (error != null) {
                        //trySend(Result.Error(error.message!!))
                        //launch { emit(Result.Error(error.message!!)) }
                        //emit(Result.Error(error.message!!))
                        trySend(Result.Error(error.message!!))
                            //.isSuccess
                    } else {
                        //trySend(Result.Success(value!!.toObjects(Complaint::class.java)))
                        //emit(Result.Success(value!!.toObjects(Complaint::class.java)))
                        //launch { emit(Result.Success(value!!.toObjects(Complaint::class.java))) }
                        //emit(Result.Success(value!!.toObjects(Complaint::class.java)))
                        trySend(Result.Success(value!!.toObjects(Complaint::class.java)))
                            //.isSuccess
                    }

                }
            awaitClose {
                Log.d(DEBUGGING, "Cancelling get complaints listener")
            }
        }
    }

}