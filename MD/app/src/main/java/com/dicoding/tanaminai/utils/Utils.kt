package com.dicoding.tanaminai.utils

import com.dicoding.tanaminai.data.remote.auth.response.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

fun extractErrorMessage(exception: Throwable): String {
    return when (exception) {
        is HttpException -> {
            try {
                val jsonInString = exception.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                errorBody.message.toString()
            } catch (e: Exception) {
                "Unknown error occurred"
            }
        }

        is IOException -> {
            "Network error occurred.\nPlease check your internet connection."
        }

        else -> {
            "An unexpected error occurred"
        }
    }
}

