package com.example.app_comp.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Complaint(
    @DocumentId
    var id: String = "",
    var userId: String = "",
    var title: String = "",
    var description: String = "",
    var images: List<String> = emptyList(),
    var date: Timestamp = Timestamp.now(),
    var location: String = "",
    var status: String = "Pending"
)
