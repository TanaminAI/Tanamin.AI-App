package com.dicoding.tanaminai.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.tanaminai.data.local.entity.BookmarkEntity
import com.dicoding.tanaminai.data.local.room.BookmarkDatabase
import com.dicoding.tanaminai.data.remote.soil.SoilApiService
import com.dicoding.tanaminai.data.remote.soil.SoilResponse
import com.dicoding.tanaminai.data.remote.weather.WeatherApiService
import com.dicoding.tanaminai.data.remote.weather.WeatherResponse
import com.dicoding.tanaminai.utils.ResultState
import com.dicoding.tanaminai.utils.extractErrorMessage
import retrofit2.HttpException

class MainRepository(
    private val soilApiService: SoilApiService,
    private val weatherApiService: WeatherApiService,
    private val bookmarkDatabase: BookmarkDatabase,
) {

    fun getSoilData(): LiveData<ResultState<SoilResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = soilApiService.getLatestSoilCondition()
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
            Log.d(TAG, "getSoilData: $errorMessage")
        } catch (e: Exception) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
            Log.d(TAG, "getSoilData: ${e.message}")
        }
    }

    fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String
    ): LiveData<ResultState<WeatherResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = weatherApiService.getCurrentWeather(lat, lon, apiKey)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
            Log.d(TAG, "getCurrentWeather: $errorMessage")
        } catch (e: Exception) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
            Log.d(TAG, "getCurrentWeather: ${e.message}")
        }
    }

    suspend fun insertBookmark(bookmark: BookmarkEntity) {
        bookmarkDatabase.bookmarkDao().insert(bookmark)
    }

    suspend fun deleteBookmark(predictAt: String) {
        bookmarkDatabase.bookmarkDao().delete(predictAt)
    }

    fun getAllBookmark(): LiveData<List<BookmarkEntity>> {
        return bookmarkDatabase.bookmarkDao().getAllBookmark()
    }

    fun checkBookmark(predictAt: String?): LiveData<Int> {
        return bookmarkDatabase.bookmarkDao().checkBookmark(predictAt)
    }

    companion object {
        private const val TAG = "MainRepository"

        fun getInstance(
            soilApiService: SoilApiService,
            weatherApiService: WeatherApiService,
            bookmarkDatabase: BookmarkDatabase
        ): MainRepository =
            MainRepository(soilApiService, weatherApiService, bookmarkDatabase)

    }
}