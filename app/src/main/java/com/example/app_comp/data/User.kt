package com.example.app_comp.data

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class User(
    @DocumentId
    var id: String = "",
    var name: String = "",
    var email: String = "",
    @get:PropertyName("is_blocked")
    @set:PropertyName("is_blocked")
    var is_blocked: Boolean = false,
    var user_role: String = ""
)

