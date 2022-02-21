package com.peterchege.blogger.api.responses

data class SearchPostResponse(
    val msg:String,
    val success:Boolean,
    val users:List<User>,
    val posts:List<Post>,
)