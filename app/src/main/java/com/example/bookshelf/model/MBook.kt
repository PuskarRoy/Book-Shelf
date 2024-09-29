package com.example.bookshelf.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class MBook(
    @Exclude var id: String? = null,
    var title: String? = null,
    var authors: String? = null,
    var notes: String? = null,
    var photoUrl: String? = null,
    var categories: String? = null,
    var publishedate: String? = null,
    var rating: Double? = null,
    var description: String? = null,
    var pageCount: String? = null,
    var startedReading: Timestamp? = null,
    var finishedReading: Timestamp? = null,
    var userId: String? = null,
    var googleBookld: String? = null
)
