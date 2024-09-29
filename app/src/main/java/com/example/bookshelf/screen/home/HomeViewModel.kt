package com.example.bookshelf.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.data.Data
import com.example.bookshelf.model.MBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FireRepository):ViewModel() {

    val data = mutableStateOf(Data<List<MBook>>(data = null, loading = false, error = null))

    init {
        getAllBooks()
    }


    private fun getAllBooks(){
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllBooks()
            if(data.value.data != null) data.value.loading = false
        }
    }


}