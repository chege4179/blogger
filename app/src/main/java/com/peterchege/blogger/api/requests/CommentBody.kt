package com.peterchege.blogger.api.requests

data class CommentBody(
    val comment:String,
    val username:String,
    val postedAt:String,
    val postedOn:String,
    val userId:String,
    val imageUrl:String,
    val postId:String,
    val postAuthor:String,
)
