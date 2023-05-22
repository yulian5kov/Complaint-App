package com.example.app_comp.utils

import android.util.Log
import com.example.app_comp.data.Complaint
import com.example.app_comp.data.User
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await


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

    // callbackFlow enforces the use of awaitClose()
    fun loginUser(email: String, password: String): Flow<Result<User>> {
        return callbackFlow {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { it ->
                    // it = AuthResult object - user auth token + other data
                    db.collection("users").document(it.user?.uid!!)
                        .get()
                        .addOnSuccessListener {
                            if(it.exists()) {
                                val user = it.toObject(User::class.java)
                                // trySend - non-suspending
                                trySend(Result.Success(user!!))
                            }else{
                                trySend(Result.Failed("User not found", "User not found"))
                            }
                        }
                        .addOnFailureListener {
                            trySend(Result.Error(it.message!!))
                        }
            }
                .addOnFailureListener {
                    trySend(Result.Error(it.message!!))
                }
            awaitClose{
                Log.d(DEBUGGING, "Cancelling login listener")
            }
        }
    }

    fun postComplaint(complaint: Complaint): Flow<Result<Complaint>>{
        return callbackFlow {
            db.collection("complaints")
                .add(complaint)
                .addOnSuccessListener {
                    trySend(Result.Success(complaint)).isSuccess
                    Log.d(DEBUGGING, "Complaint added successfully")
                }
                .addOnFailureListener {
                    trySend(Result.Error(it.message!!)).isSuccess
                }
            awaitClose {
                Log.d(DEBUGGING, "Cancelling add complaint listener")
            }
        }
    }

    fun getComplaintsByUser(userId: String): Flow<Result<List<Complaint>>> {
        return callbackFlow {
            val query = db.collection("complaints")
                .whereEqualTo("userId", userId)

            val registration = query.addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    trySend(Result.Error(exception.message ?: "Failed to retrieve user complaints")).isSuccess
                    return@addSnapshotListener
                }

                val complaints = mutableListOf<Complaint>()
                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        val complaint = document.toObject(Complaint::class.java)
                        complaints.add(complaint)
                    }
                }

                trySend(Result.Success(complaints)).isSuccess
                Log.d(DEBUGGING, "Received user complaints update")
            }

            awaitClose {
                registration.remove()
                Log.d(DEBUGGING, "Canceled get user complaints listener")
            }
        }
    }

    fun updateComplaintStatus(complaintId: String, newStatus: String): Flow<Result<Unit>> {
        return callbackFlow {
            val complaintRef = db.collection("complaints").document(complaintId)
            val updates = mapOf("status" to newStatus)

            val registration = complaintRef.update(updates).addOnSuccessListener {
                trySend(Result.Success(Unit))
            }.addOnFailureListener { exception ->
                trySend(Result.Error(exception.toString()))
            }

            awaitClose {
//                registration.remove()
            }
        }
    }


    fun getAllComplaints(): Flow<Result<List<Complaint>>> {
        return callbackFlow {
            val query = db.collection("complaints")

            val registration = query.addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    trySend(Result.Error(exception.message ?: "Failed to retrieve complaints")).isSuccess
                    return@addSnapshotListener
                }

                val complaints = mutableListOf<Complaint>()
                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        val complaint = document.toObject(Complaint::class.java)
                        complaints.add(complaint)
                    }
                }

                trySend(Result.Success(complaints)).isSuccess
                Log.d(DEBUGGING, "Received all complaints update")
            }

            awaitClose {
                registration.remove()
                Log.d(DEBUGGING, "Canceled get all complaints listener")
            }
        }
    }


    fun getComplaintById(complaintId: String): Flow<Result<Complaint>> {
        return callbackFlow {
            db.collection("complaints")
                .document(complaintId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val complaint = documentSnapshot.toObject(Complaint::class.java)
                    if (complaint != null) {
                        trySend(Result.Success(complaint)).isSuccess
                        Log.d(DEBUGGING, "Retrieved complaint details successfully")
                    } else {
                        trySend(Result.Error("Complaint not found")).isSuccess
                        Log.d(DEBUGGING, "Complaint not found")
                    }
                }
                .addOnFailureListener { exception ->
                    trySend(Result.Error(exception.message ?: "Failed to retrieve complaint details")).isSuccess
                    Log.d(DEBUGGING, "Failed to retrieve complaint details")
                }
            awaitClose {
                Log.d(DEBUGGING, "Cancelling get complaint details listener")
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

//    fun getComplaintById(id: String): Flow<Result<Complaint>> = callbackFlow {
//        val docRef = db.collection("complaints").document(id)
//        val subscription = docRef.addSnapshotListener { snapshot, error ->
//            if (error != null) {
//                trySend(Result.Error(error.message ?: "Unknown error"))
//            } else if (snapshot != null && snapshot.exists()) {
//                val complaint = snapshot.toObject(Complaint::class.java)
//                if (complaint != null) {
//                    trySend(Result.Success(complaint))
//                } else {
//                    trySend(Result.Error("Failed to convert snapshot to Complaint"))
//                }
//            } else {
//                trySend(Result.Error("No such document"))
//            }
//        }
//
//        awaitClose { subscription.remove() }
//    }
//
//    fun updateComplaint(complaint: Complaint): Flow<Result<Unit>> = callbackFlow {
//        val docRef = db.collection("complaints").document(complaint.id)
//        docRef.set(complaint)
//            .addOnSuccessListener {
//                trySend(Result.Success(Unit))
//            }
//            .addOnFailureListener { exception ->
//                trySend(Result.Error(exception.message ?: "Unknown error"))
//            }
//
//        awaitClose { }
//    }

}