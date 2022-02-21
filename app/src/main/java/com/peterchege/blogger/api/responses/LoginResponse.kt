package com.peterchege.blogger.api.responses

data class LoginResponse(
    val msg: String,
    val success: Boolean,
    val user: User?
)