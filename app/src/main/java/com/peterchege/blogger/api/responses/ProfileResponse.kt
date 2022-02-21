package com.peterchege.blogger.api.responses

data class ProfileResponse(
    val msg:String,
    val success:Boolean,
    val user:User,
    val posts:List<Post>,
)
