package com.dicoding.tanaminai.utils.di

import android.content.Context
import com.dicoding.tanaminai.data.local.room.BookmarkDatabase
import com.dicoding.tanaminai.data.remote.auth.remote.AuthApiConfig
import com.dicoding.tanaminai.repository.AuthRepository
import com.dicoding.tanaminai.data.pref.UserPreferences
import com.dicoding.tanaminai.data.pref.dataStore
import com.dicoding.tanaminai.data.remote.soil.SoilApiConfig
import com.dicoding.tanaminai.data.remote.weather.WeatherApiConfig
import com.dicoding.tanaminai.repository.MainRepository

object Injection {
    fun provideMainRepository(context: Context): MainRepository {
        val soilApiService = SoilApiConfig.getSoilApiService()
        val weatherApiService = WeatherApiConfig.getWeatherApiService()
        val bookmarkDatabase = BookmarkDatabase.getDatabase(context)
        return MainRepository.getInstance(soilApiService, weatherApiService, bookmarkDatabase)
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = AuthApiConfig.getAuthApiService()
        return AuthRepository.getInstance(pref, apiService)
    }
}