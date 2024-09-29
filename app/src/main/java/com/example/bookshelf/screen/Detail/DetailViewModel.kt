package com.example.bookshelf.screen.Detail

import android.content.ClipData.Item
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.Data
import com.example.bookshelf.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository):ViewModel() {

    val data = mutableStateOf(Data<com.example.bookshelf.model.Item>(data = null, loading = false, error = null))

    fun getBookInfo(bookId:String){

        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getBook(bookId)
            data.value.loading = false
        }
    }
}