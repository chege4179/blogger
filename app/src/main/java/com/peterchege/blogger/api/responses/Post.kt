package com.peterchege.blogger.api.responses

import com.peterchege.blogger.room.entities.PostRecord


data class Post(
    val _id: String,
    val postTitle: String,
    val postBody:String,
    val postAuthor:String,
    val imageUrl: String,
    val postedAt: String,
    val postedOn: String,
    val comments:List<Comment>,
    val views:List<View>,
    val likes:List<Like>

    )

fun Post.toPostRecord():PostRecord{
    return PostRecord(
        _id,
        postTitle,
        postBody,
        imageUrl,
        postedAt,
        postAuthor,
        postedOn,
    )
}