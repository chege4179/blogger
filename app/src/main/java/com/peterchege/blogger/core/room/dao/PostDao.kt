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
package com.peterchege.blogger.core.room.dao

import androidx.room.*
import com.peterchege.blogger.core.api.responses.Comment
import com.peterchege.blogger.core.api.responses.Like
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.View
import com.peterchege.blogger.core.room.entities.*

import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Transaction
    @Query("SELECT * FROM post")
    fun getAllLocalPosts(): Flow<List<PostRecordWithCommentsLikesViews>>

    @Query("SELECT * FROM post WHERE _id = :id")
    suspend fun getPostById(id: String): PostRecordWithCommentsLikesViews?


    @Transaction
    suspend fun insertPost(post: Post) {
        val postEntity = PostRecord(
            _id = post._id,
            postTitle = post.postTitle,
            postBody = post.postBody,
            postAuthor = post.postAuthor,
            ImageUrl = post.imageUrl,
            postedAt = post.postedAt,
            postedOn = post.postedOn
        )

        insertPostWithComments(postEntity, post.comments)
        insertPostWithViews(postEntity, post.views)
        insertPostWithLikes(postEntity, post.likes)
    }


    @Query("DELETE FROM post WHERE _id = :id")
    suspend fun deletePostById(id: String)

    @Query("DELETE FROM post")
    suspend fun deleteAllPosts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostWithComments(postEntity: PostRecord, comments: List<Comment>) {
        val commentEntities = comments.map {
            CommentEntity(
                postId = postEntity._id,
                username = it.username,
                userId = it.userId,
                imageUrl = it.imageUrl,
                id = it.commentId,
                postedAt = it.postedAt,
                postedOn = it.postedOn,
                comment = it.comment

            )
        }
        insertComments(comments = commentEntities)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostWithViews(postEntity: PostRecord, views: List<View>) {
        val viewEntities = views.map {
            ViewEntity(
                postId = postEntity._id,
                viewerFullname = it.viewerFullname,
                viewerId = it.viewerId,
                viewerUsername = it.viewerUsername
            )
        }
        insertViews(views = viewEntities)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertViews(views: List<ViewEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostWithLikes(postEntity: PostRecord, likes: List<Like>) {
        val likeEntities = likes.map {
            LikeEntity(
                username = it.username,
                postId = postEntity._id,
                id = it.id,
                userId = it.userId,
            )
        }
        insertLikes(likes = likeEntities)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikes(likes: List<LikeEntity>)
}