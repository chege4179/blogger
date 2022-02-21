package com.peterchege.blogger.api.responses

data class Follower(
    val followerUsername:String,
    val followerFullname:String,
    val followerId:String,

)
data class Following(
    val followedUsername: String,
    val followedFullname:String,
    val followedId:String,

)