package com.dicoding.tanaminai.view.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tanaminai.data.local.entity.BookmarkEntity
import com.dicoding.tanaminai.repository.MainRepository
import kotlinx.coroutines.launch

class BookmarkViewModel(private val repository: MainRepository) : ViewModel() {

    fun insert(bookmark: BookmarkEntity) {
        viewModelScope.launch {
            repository.insertBookmark(bookmark)
        }
    }

    fun delete(predictAt: String) {
        viewModelScope.launch {
            repository.deleteBookmark(predictAt)
        }
    }

    fun getAllBookmark(): LiveData<List<BookmarkEntity>> = repository.getAllBookmark()

    fun checkBookmark(predictAt: String): LiveData<Int> {
        return repository.checkBookmark(predictAt)
    }

}