package com.example.bookshelf.repository

import com.example.bookshelf.data.Data
import com.example.bookshelf.model.Item
import com.example.bookshelf.network.BookApi
import javax.inject.Inject

class Repository @Inject constructor(private val bookApi: BookApi) {


    suspend fun getAllBooks(search: String): Data<List<Item>> {
        val dataList = Data<List<Item>>(data = emptyList(), loading = false, error = null)
        try {
            dataList.loading = true
            dataList.data = bookApi.getAllBook(search).items
            if (dataList.data!!.isNotEmpty()) dataList.loading = false
        } catch (e: Exception) {
            dataList.error = e
            dataList.loading = false
        }

        return dataList
    }

    suspend fun getBook(book: String): Data<Item> {
        val dataItem = Data<Item>(loading = false, error = null, data = null)
        try {
            dataItem.loading = true
            dataItem.data = bookApi.getBook(book)
            if (dataItem.data != null) dataItem.loading = false
        } catch (e: Exception) {
            dataItem.error = e
            dataItem.loading = false
        }

        return dataItem
    }
}