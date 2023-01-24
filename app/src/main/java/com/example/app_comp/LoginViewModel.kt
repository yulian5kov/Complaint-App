package com.example.app_comp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LoginViewModel : ViewModel() {

    fun loginUser(email: String, password: String): Flow<ResultData<User>> {
        return callbackFlow {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    db.collection("users").document(mAuth.currentUser!!.uid).get()
                        .addOnSuccessListener { documentSnapshot ->
                            val user = documentSnapshot.toObject<User>()
                            if (user != null) {
                                trySend(ResultData.Success(user)).isSuccess
                            }
                        }.addOnFailureListener { e ->
                            mAuth.signOut()
                            trySend(ResultData.Exception(e))
                            trySend(ResultData.Failed(message = R.string.user_login_failure, error = e.message.toString())).isFailure
                            cancel(
                                message = "Error Adding User",
                                cause = e
                            )
                            return@addOnFailureListener
                        }
                } else {
                    trySend(ResultData.Exception(task.exception))
                    trySend(ResultData.Failed(message = R.string.user_login_failure, error = task.exception?.message.toString())).isFailure
                    cancel(
                        message = "createUserWithEmail:failure",
                        cause = task.exception
                    )
                    return@addOnCompleteListener
                }
            }
            awaitClose {
                Log.d(DEBUGGING, "Cancelling posts listener")
            }
        }
    }

    fun addUser(user: User, password: String): Flow<ResultData<User>>  {
        return callbackFlow {
            mAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val resultUser = task.result.user
                        if(resultUser != null){
                            user.id = resultUser.uid
                            db.collection("users")
                                .document(user.id)
                                .set(user)
                                .addOnSuccessListener {
                                    Log.i(DEBUGGING, "User added successfully")
                                    trySend(ResultData.Success(user)).isSuccess
                                }
                                .addOnFailureListener { e ->
                                    Log.w(DEBUGGING, "Error Adding User!! ", e)
                                    trySend(ResultData.Failed(message = R.string.user_add_failure, error = e.message.toString())).isFailure
                                    cancel(
                                        message = "Error Adding User",
                                        cause = e
                                    )
                                    return@addOnFailureListener
                                }
                        }
                    }
                }
        }
    }

}