package com.dicoding.tanaminai.view.auth.signin

import androidx.lifecycle.ViewModel
import com.dicoding.tanaminai.repository.AuthRepository

class SignInViewModel(private val repository: AuthRepository) : ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)
}