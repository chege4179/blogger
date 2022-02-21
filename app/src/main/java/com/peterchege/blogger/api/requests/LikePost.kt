package com.peterchege.blogger.api.requests

data class LikePost (
    val userId:String,
    val username:String,
    val fullname:String,
    val postAuthor:String,
    val postId:String,
        )