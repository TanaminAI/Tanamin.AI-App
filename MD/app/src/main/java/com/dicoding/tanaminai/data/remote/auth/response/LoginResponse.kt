package com.dicoding.tanaminai.data.remote.auth.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("message")
    val message: String? = null
)

data class Data(

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("token")
    val token: String
)
