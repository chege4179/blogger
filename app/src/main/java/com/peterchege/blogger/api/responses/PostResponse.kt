package com.peterchege.blogger.api.responses

data class PostResponse(
    val msg: String,
    val post: Post?,
    val success: Boolean
)