package com.dicoding.tanaminai.view.home

import androidx.lifecycle.ViewModel
import com.dicoding.tanaminai.repository.MainRepository

class HomeViewModel(private val repository: MainRepository) : ViewModel() {

    fun getSoilData() = repository.getSoilData()

    fun getCurrentWeather(lat: Double, lon: Double, apiKey: String) =
        repository.getCurrentWeather(lat, lon, apiKey)

}