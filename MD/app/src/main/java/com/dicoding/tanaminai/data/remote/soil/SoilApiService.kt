package com.dicoding.tanaminai.data.remote.soil

import retrofit2.http.GET

interface SoilApiService {

    @GET("get-latest-soil-condition")
    suspend fun getLatestSoilCondition(): SoilResponse

}