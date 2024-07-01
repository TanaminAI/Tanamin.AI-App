package com.dicoding.tanaminai.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.tanaminai.repository.AuthRepository
import com.dicoding.tanaminai.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AuthRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}

