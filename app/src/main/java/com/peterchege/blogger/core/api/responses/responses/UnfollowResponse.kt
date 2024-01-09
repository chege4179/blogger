package com.peterchege.blogger.core.api.responses.responses

import kotlinx.serialization.Serializable


@Serializable
data class UnfollowResponse(
    val msg:String,
    val success:Boolean,
)