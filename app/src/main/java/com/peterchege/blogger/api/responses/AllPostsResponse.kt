package com.peterchege.blogger.api.responses

data class AllPostsResponse(
    val msg: String,
    val posts: List<Post>,
    val success: Boolean
)