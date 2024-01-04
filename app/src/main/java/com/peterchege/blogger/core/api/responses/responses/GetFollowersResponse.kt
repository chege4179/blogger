package com.peterchege.blogger.core.api.responses.responses

import com.peterchege.blogger.core.api.responses.models.User
import kotlinx.serialization.Serializable


@Serializable
data class GetFollowersResponse(
    val msg:String,
    val success:Boolean,
    val followersCount:Int?,
    val nextPage:Int?,
    val followers:List<User>?
)