package com.example.app_comp

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object FirebaseProfileService {

    val mAuth: FirebaseAuth get() = FirebaseAuth.getInstance()
    val db: FirebaseFirestore get() = FirebaseFirestore.getInstance()

    fun addUser(user: User, password: String): Flow<User>{
        return callbackFlow{
            mAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        if(task.result.user != null){
                            user.id = task.result.user!!.uid
                            db.collection("users")
                                .document(user.id)
                                .set(user)
                                .addOnSuccessListener{
                                    Log.i("debugging", "User added successfully")
                                }
                                .addOnFailureListener{e->
                                    Log.w("debugging", "Error Adding User!! ", e)
                                }
                        }else{
                            Log.e("debugging", "addUser: result user null")
                        }
                    }
                }
        }
    }

}