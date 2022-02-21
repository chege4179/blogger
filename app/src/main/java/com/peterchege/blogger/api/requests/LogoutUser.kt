package com.peterchege.blogger.api.requests

data class LogoutUser(
    val username:String,
    val token:String,
    val id:String,
)
