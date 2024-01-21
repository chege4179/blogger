package com.peterchege.blogger.core.api.responses.responses

import com.peterchege.blogger.core.api.responses.models.Post
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePostResponse(
    val msg:String,
    val success:Boolean,
    val post:Post?
)