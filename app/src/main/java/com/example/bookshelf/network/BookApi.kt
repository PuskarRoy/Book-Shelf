package com.example.bookshelf.network

import com.example.bookshelf.model.Book
import com.example.bookshelf.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BookApi {
    @GET("books/v1/volumes")
    suspend fun getAllBook(@Query("q") q:String = "flutter"):Book

    @GET("books/v1/volumes/{bookId}")
    suspend fun getBook(@Path("bookId") bookId : String) : Item
}