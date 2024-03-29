package com.peterchege.blogger.core.api.responses.responses

import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserResponse(
    val msg:String,
    val success:Boolean,
    val user: User?
)