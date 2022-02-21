package com.peterchege.blogger.api.responses

data class Comment (
    val comment:String,
    val username:String,
    val postedAt:String,
    val postedOn:String,
    val userId:String,
    val imageUrl:String,
    val id:String,
)