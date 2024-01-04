package com.peterchege.blogger.core.api.responses.responses

import com.peterchege.blogger.core.api.responses.models.Post
import kotlinx.serialization.Serializable

@Serializable
data class GetPostsByUserIdResponse(
    val msg:String,
    val success:Boolean,
    val nextPage:Int?,
    val totalPosts:Int?,
    val posts:List<Post>?,
)