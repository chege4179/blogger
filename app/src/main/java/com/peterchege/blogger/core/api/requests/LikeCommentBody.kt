package com.peterchege.blogger.core.api.requests

import kotlinx.serialization.Serializable


@Serializable
data class LikeCommentBody(
    val commentId:String,
    val userId:String,
)