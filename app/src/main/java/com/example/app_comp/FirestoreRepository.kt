package com.example.app_comp

import android.util.Log
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
                                trySend(Result.Failed("User not found", "user not found")).isSuccess
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
                        Log.d(DEBUGGING, "ofl: Error adding complaint: ${it.message}")
                        trySend(Result.Error(it.message!!)).isSuccess
                    }
            } catch (e: Exception) {
                Log.d(DEBUGGING, "e: Error adding complaint: ${e.message}")
                trySend(Result.Error(e.message!!)).isSuccess
            }
            awaitClose {
                Log.d(DEBUGGING, "Cancelling post complaint listener")
            }
        }
    }

    fun getComplaints(): Flow<Result<List<Complaint>>> {
        return callbackFlow {
            try {
                db.collection("complaints")
                    .get()
                    .addOnSuccessListener { result ->
                        trySend(Result.Success(result.toObjects(Complaint::class.java)))
                    }
                    .addOnFailureListener { exception ->
                        trySend(Result.Error(exception.message!!))
                    }
            } catch (e: Exception) {
                trySend(Result.Error(e.message!!))
            }
            awaitClose {
                Log.d(DEBUGGING, "Cancelling get complaints listener")
            }
        }
    }

    fun getComplaintById(id: String): Flow<Result<Complaint>> = callbackFlow {
        val docRef = db.collection("complaints").document(id)
        val subscription = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.Error(error.message ?: "Unknown error"))
            } else if (snapshot != null && snapshot.exists()) {
                val complaint = snapshot.toObject(Complaint::class.java)
                if (complaint != null) {
                    trySend(Result.Success(complaint))
                } else {
                    trySend(Result.Error("Failed to convert snapshot to Complaint"))
                }
            } else {
                trySend(Result.Error("No such document"))
            }
        }

        awaitClose { subscription.remove() }
    }

    fun updateComplaint(complaint: Complaint): Flow<Result<Unit>> = callbackFlow {
        val docRef = db.collection("complaints").document(complaint.id)
        docRef.set(complaint)
            .addOnSuccessListener {
                trySend(Result.Success(Unit))
            }
            .addOnFailureListener { exception ->
                trySend(Result.Error(exception.message ?: "Unknown error"))
            }

        awaitClose { }
    }

}