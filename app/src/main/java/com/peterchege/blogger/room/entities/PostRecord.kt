package com.peterchege.blogger.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peterchege.blogger.api.responses.Post


@Entity(tableName = "post")
data class PostRecord(
    @PrimaryKey
    val _id: String,
    val postTitle: String,
    val postBody: String,
    val ImageUrl: String,
    val postedAt: String,
    val postAuthor: String,
    val postedOn: String,
)

fun PostRecord.toPost():Post{
    return Post(
        _id,
        postTitle,
        postBody,
        postAuthor,
        imageUrl = ImageUrl,
        postedAt,
        postedOn,
        emptyList(),
        emptyList(),
        emptyList(),

    )

}