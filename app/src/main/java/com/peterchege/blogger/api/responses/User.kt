package com.peterchege.blogger.api.responses

import com.peterchege.blogger.api.requests.Notification

data class User(
    val _id: String,
    val email: String,
    val fullname: String,
    val imageUrl: String,
    val password: String,
    val username: String,
    val followers: List<Follower>,
    val following: List<Following>,
    val tokens: List<String>,
    val notifications:List<Notification>,
    val __v: Int,
)