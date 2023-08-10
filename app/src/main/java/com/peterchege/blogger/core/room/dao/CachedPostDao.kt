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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.peterchege.blogger.core.api.responses.Comment
import com.peterchege.blogger.core.api.responses.Like
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.View
import com.peterchege.blogger.core.room.entities.CommentCacheEntity
import com.peterchege.blogger.core.room.entities.CommentEntity
import com.peterchege.blogger.core.room.entities.LikeCacheEntity
import com.peterchege.blogger.core.room.entities.LikeEntity
import com.peterchege.blogger.core.room.entities.PostCacheRecord
import com.peterchege.blogger.core.room.entities.PostCacheRecordWithCommentsLikesViews
import com.peterchege.blogger.core.room.entities.PostRecord
import com.peterchege.blogger.core.room.entities.PostRecordWithCommentsLikesViews
import com.peterchege.blogger.core.room.entities.ViewCacheEntity
import com.peterchege.blogger.core.room.entities.ViewEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CachedPostDao {


    @Transaction
    @Query("SELECT * FROM postCache")
    fun getAllLocalPosts(): Flow<List<PostCacheRecordWithCommentsLikesViews>>

    @Query("SELECT * FROM postCache WHERE _id = :postId")
    fun getCachedPostById(postId:String):Flow<PostCacheRecordWithCommentsLikesViews?>

    @Query("DELETE FROM postCache")
    suspend fun deleteAllPostsFromCache()

    @Transaction
    suspend fun insertPostCache(post: Post) {
        val postEntity = PostCacheRecord(
            _id = post._id,
            postTitle = post.postTitle,
            postBody = post.postBody,
            postAuthor = post.postAuthor,
            ImageUrl = post.imageUrl,
            postedAt = post.postedAt,
            postedOn = post.postedOn
        )
        insertPostCacheRecord(post = postEntity)
        insertPostCacheWithComments(postEntity = postEntity,comments =  post.comments)
        insertPostCacheWithViews(postEntity = postEntity,views =  post.views)
        insertPostCacheWithLikes(postEntity = postEntity,likes =  post.likes)
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostCacheRecord(post:PostCacheRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostCacheWithComments(postEntity: PostCacheRecord, comments: List<Comment>) {
        val commentEntities = comments.map {
            CommentCacheEntity(
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
        insertCacheComments(comments = commentEntities)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCacheComments(comments: List<CommentCacheEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostCacheWithViews(postEntity: PostCacheRecord, views: List<View>) {
        val viewEntities = views.map {
            ViewCacheEntity(
                postId = postEntity._id,
                viewerFullname = it.viewerFullname,
                viewerId = it.viewerId,
                viewerUsername = it.viewerUsername
            )
        }
        insertCacheViews(views = viewEntities)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCacheViews(views: List<ViewCacheEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostCacheWithLikes(postEntity: PostCacheRecord, likes: List<Like>) {
        val likeEntities = likes.map {
            LikeCacheEntity(
                username = it.username,
                postId = postEntity._id,
                fullname = it.fullname,
                userId = it.userId,
            )
        }
        insertCacheLikes(likes = likeEntities)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCacheLikes(likes: List<LikeCacheEntity>)









}