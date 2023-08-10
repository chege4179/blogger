package com.peterchege.blogger.domain.models

import com.peterchege.blogger.core.api.responses.Comment
import com.peterchege.blogger.core.api.responses.Like
import com.peterchege.blogger.core.api.responses.View

data class PostUI(
    val _id: String,
    val postTitle: String,
    val postBody:String,
    val postAuthor:String,
    val imageUrl: String,
    val postedAt: String,
    val postedOn: String,
    val comments:List<Comment>,
    val views:List<View>,
    val likes:List<Like>,
    val isSaved:Boolean = false,
    val isLiked:Boolean = false,
    val isProfile:Boolean = false,
)