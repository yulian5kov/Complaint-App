package com.example.app_comp

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Complaint(
    @DocumentId
    var id: String = "",
    var userId: String = "",
    var text: String = "",
    var images: List<String> = emptyList(),
    var date: Timestamp = Timestamp.now()
)
