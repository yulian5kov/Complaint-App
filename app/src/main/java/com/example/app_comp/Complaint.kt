package com.example.app_comp

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Complaint(
    @DocumentId
    var id: String = "",
    var userId: String = "",
    var title: String = "",
    var description: String = "",
    val images: List<String> = emptyList(),
    var date: Timestamp = Timestamp.now()
)
