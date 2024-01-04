package com.peterchege.blogger.core.api.responses.responses

import com.peterchege.blogger.core.api.responses.models.Like
import com.peterchege.blogger.core.api.responses.models.User
import kotlinx.serialization.Serializable

@Serializable
data class GetUserLikeResponse(
    val msg: String,
    val success: Boolean,
    val user: User?,
    val likes: List<Like>?
)