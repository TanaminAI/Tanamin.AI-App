package com.dicoding.tanaminai.view.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.tanaminai.repository.AuthRepository
import kotlinx.coroutines.launch


class SettingsViewModel(private val repository: AuthRepository) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return repository.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            repository.saveThemeSetting(isDarkModeActive)
        }
    }
}