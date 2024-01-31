package com.peterchege.blogger.core.api.responses.responses

import com.peterchege.blogger.core.api.responses.models.CommentLike
import kotlinx.serialization.Serializable

@Serializable
data class LikeCommentResponse(
    val msg:String,
    val success:Boolean,
    val commentLike: CommentLike?
)