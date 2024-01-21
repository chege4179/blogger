package com.peterchege.blogger.core.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class UnFollowUser(
    val userId: String,
    val unfollowedUserId: String,
)