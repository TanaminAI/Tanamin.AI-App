package com.dicoding.tanaminai.data.pref

data class UserModel(
    val email: String,
    val token: String,
    var isLogin: Boolean = false
)

