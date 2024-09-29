package com.example.bookshelf.data

import com.example.bookshelf.model.Item

data class Data<T>(
    var data: T?,
    var loading: Boolean,
    var error: Exception?
)