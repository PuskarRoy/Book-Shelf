package com.example.bookshelf.screen.home

import com.example.bookshelf.data.Data
import com.example.bookshelf.model.MBook
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FireRepository {

    suspend fun getAllBooks():Data<List<MBook>>{

        val data = Data<List<MBook>>(data = null, loading = false, error = null)

        try {
            data.loading = true
            data.data = FirebaseFirestore.getInstance().collection("Books").get().await().documents.map {

                it.toObject(MBook::class.java)!!
            }
        }
        catch (e:Exception){
            data.error = e
            data.loading = false
        }

        return data

    }

}