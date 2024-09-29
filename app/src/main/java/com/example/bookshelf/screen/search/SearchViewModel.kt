package com.example.bookshelf.screen.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.Data
import com.example.bookshelf.model.Item
import com.example.bookshelf.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val listOfBook = mutableStateOf(Data<List<Item>>(data = emptyList(), loading = false, error = null))

    init {
        searchBooks(search = "any")
    }

    fun searchBooks(search: String) {


        viewModelScope.launch {
            listOfBook.value.loading = true
            listOfBook.value = repository.getAllBooks(search)
            listOfBook.value.loading = false
        }

    }
}