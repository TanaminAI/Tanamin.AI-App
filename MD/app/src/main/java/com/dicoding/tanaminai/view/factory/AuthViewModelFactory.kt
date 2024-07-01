package com.dicoding.tanaminai.view.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.tanaminai.repository.AuthRepository
import com.dicoding.tanaminai.utils.di.Injection
import com.dicoding.tanaminai.view.auth.signin.SignInViewModel
import com.dicoding.tanaminai.view.auth.signup.SignUpViewModel
import com.dicoding.tanaminai.view.main.MainViewModel
import com.dicoding.tanaminai.view.settings.SettingsViewModel

class AuthViewModelFactory(private val repository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context): AuthViewModelFactory =
            synchronized(this) {
                AuthViewModelFactory(Injection.provideAuthRepository(context))
            }
    }
}