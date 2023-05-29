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

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "postCache")
data class PostCacheRecord(
    @PrimaryKey
    val _id: String,
    val postTitle: String,
    val postBody: String,
    val ImageUrl: String,
    val postedAt: String,
    val postAuthor: String,
    val postedOn: String,
)

data class PostCacheRecordWithCommentsLikesViews(
    @Embedded val post: PostCacheRecord,


    @Relation(
        parentColumn = "_id",
        entityColumn = "postId"
    )
    val comments: List<CommentCacheEntity>,

    @Relation(
        parentColumn = "_id",
        entityColumn = "postId"
    )
    val views: List<ViewCacheEntity>,

    @Relation(
        parentColumn = "_id",
        entityColumn = "postId"
    )
    val likes: List<LikeCacheEntity>

)


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PostCacheRecord::class,
            parentColumns = ["_id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CommentCacheEntity(
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

@Entity(foreignKeys = [
    ForeignKey(
        entity = PostCacheRecord::class,
        parentColumns = ["_id"],
        childColumns = ["postId"],
        onDelete = ForeignKey.CASCADE
    )
]
)
data class ViewCacheEntity(
    @PrimaryKey
    val viewerId:String,
    val viewerUsername:String,
    val viewerFullname:String,

    val postId: String,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PostCacheRecord::class,
            parentColumns = ["_id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LikeCacheEntity(
    @PrimaryKey
    val fullname: String,
    val username: String,
    val userId: String,

    val postId: String,
)


