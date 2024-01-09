package com.peterchege.blogger.core.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peterchege.blogger.core.api.responses.models.UserCount

@Entity(tableName = "commentLike")
data class CommentLikeEntity(
    @PrimaryKey
    val likeCommentId:String,
    val userId: String,
)