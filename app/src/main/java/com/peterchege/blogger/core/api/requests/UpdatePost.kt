package com.peterchege.blogger.core.api.requests

import kotlinx.serialization.Serializable


@Serializable
data class UpdatePost(
    val postId: String,
    val postTitle: String,
    val postBody: String
)