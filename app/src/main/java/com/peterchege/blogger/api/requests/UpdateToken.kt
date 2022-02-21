package com.peterchege.blogger.api.requests

data class UpdateToken(
    val newToken:String,
    val oldToken:String,
    val userId:String,
)
