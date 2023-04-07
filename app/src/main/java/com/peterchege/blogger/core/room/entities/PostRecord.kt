/*
 * Copyright 2023 Blogger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peterchege.blogger.core.room.entities

import androidx.room.*
import com.peterchege.blogger.core.api.responses.Post


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

data class PostRecordWithCommentsLikesViews(
    @Embedded val post: PostRecord,


    @Relation(
        parentColumn = "_id",
        entityColumn = "postId"
    )
    val comments: List<CommentEntity>,

    @Relation(
        parentColumn = "_id",
        entityColumn = "postId"
    )
    val views: List<ViewEntity>,

    @Relation(
        parentColumn = "_id",
        entityColumn = "postId"
    )
    val likes: List<LikeEntity>

)


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PostRecord::class,
            parentColumns = ["_id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CommentEntity(
    @PrimaryKey
    val id: String,
    val postId: String,

    val comment: String,
    val username: String,
    val postedAt: String,
    val postedOn: String,
    val userId: String,
    val imageUrl: String,


    )

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PostRecord::class,
            parentColumns = ["_id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ViewEntity(
    @PrimaryKey
    val viewerId:String,
    val viewerUsername:String,
    val viewerFullname:String,

    val postId: String,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PostRecord::class,
            parentColumns = ["_id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LikeEntity(
    @PrimaryKey
    val id: String,
    val username: String,
    val userId: String,

    val postId: String,
)


