package com.dicoding.tanaminai.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.tanaminai.data.remote.auth.remote.AuthApiService
import com.dicoding.tanaminai.data.remote.auth.response.LoginResponse
import com.dicoding.tanaminai.data.remote.auth.response.RegisterResponse
import com.dicoding.tanaminai.data.pref.UserModel
import com.dicoding.tanaminai.data.pref.UserPreferences
import com.dicoding.tanaminai.utils.ResultState
import com.dicoding.tanaminai.utils.extractErrorMessage
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class AuthRepository(
    private val pref: UserPreferences,
    private val apiService: AuthApiService
) {
    private suspend fun saveSession(user: UserModel) {
        pref.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return pref.getSession()
    }

    suspend fun logout() {
        pref.logout()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        pref.saveThemeSetting(isDarkModeActive)
    }

    fun getThemeSetting(): Flow<Boolean> {
        return pref.getThemeSetting()
    }

    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.login(email, password)
            saveSession(UserModel(email, response.data.token, true))
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
            Log.d(TAG, "login: $errorMessage")
        } catch (e: Exception) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
            Log.d(TAG, "login: ${e.message}")
        }
    }

    fun register(
        first_name: String,
        last_name: String,
        email: String,
        password: String
    ): LiveData<ResultState<RegisterResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.register(first_name, last_name, email, password)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
            Log.d(TAG, "register: $errorMessage")
        } catch (e: Exception) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
            Log.d(TAG, "register: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "AuthRepository"

        fun getInstance(
            userPreference: UserPreferences,
            apiService: AuthApiService
        ): AuthRepository =
            AuthRepository(userPreference, apiService)

    }
}