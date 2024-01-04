package com.peterchege.blogger.core.api.responses.responses

import com.peterchege.blogger.core.api.responses.models.User
import kotlinx.serialization.Serializable

@Serializable
data class GetPostLikesResponse(
    val msg:String,
    val success:Boolean,
    val nextPage:Int?,
    val likesCount:Int?,
    val likes:List<User>?

)