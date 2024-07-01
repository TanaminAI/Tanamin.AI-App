package com.dicoding.tanaminai.data.remote.auth.remote

import com.dicoding.tanaminai.data.remote.auth.response.LoginResponse
import com.dicoding.tanaminai.data.remote.auth.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiService {

    @FormUrlEncoded
    @POST("/api/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("/api/auth/register")
    suspend fun register(
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse
}