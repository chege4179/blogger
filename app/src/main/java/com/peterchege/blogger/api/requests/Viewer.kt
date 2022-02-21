package com.peterchege.blogger.api.requests

data class Viewer (
    val viewerUsername:String,
    val viewerFullname:String,
    val viewerId:String,
    val postId:String,

        )