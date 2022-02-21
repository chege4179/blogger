package com.peterchege.blogger.api.requests

data class FollowUser (
    val followerUsername: String,
    val followerFullname: String,
    val followerId: String,
    val followedUsername: String,

    )