package com.peterchege.blogger.api.responses

data class CommentResponse (
    val msg:String,
    val success:Boolean,
    val comment: Comment,

    )