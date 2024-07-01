package com.dicoding.tanaminai.view.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.tanaminai.repository.MainRepository
import com.dicoding.tanaminai.utils.di.Injection
import com.dicoding.tanaminai.view.bookmark.BookmarkViewModel
import com.dicoding.tanaminai.view.home.HomeViewModel


class MainViewModelFactory(private val repository: MainRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
                BookmarkViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context): MainViewModelFactory =
            synchronized(this) {
                MainViewModelFactory(Injection.provideMainRepository(context))
            }
    }
}