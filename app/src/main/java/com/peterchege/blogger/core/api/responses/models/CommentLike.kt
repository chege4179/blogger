package com.peterchege.blogger.core.api.responses.models

import kotlinx.serialization.Serializable

@Serializable
data class CommentLike(
    val likeCommentId:String,
    val userId:String,
)