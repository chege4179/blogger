package com.peterchege.blogger.core.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedPostsResponse(
    val msg: String,
    val success: Boolean,
    val posts: List<Post>?,
    val nextPage:Int?,
)