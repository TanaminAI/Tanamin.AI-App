package com.dicoding.tanaminai.view.auth.signup

import androidx.lifecycle.ViewModel
import com.dicoding.tanaminai.repository.AuthRepository

class SignUpViewModel(private val repository: AuthRepository) : ViewModel() {
    fun register(name: String, lastName: String, email: String, password: String) =
        repository.register(name, lastName, email, password)
}