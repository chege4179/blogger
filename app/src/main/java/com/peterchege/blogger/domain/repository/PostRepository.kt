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
package com.peterchege.blogger.domain.repository

import com.peterchege.blogger.core.api.requests.FollowUser
import com.peterchege.blogger.core.api.requests.LikePost
import com.peterchege.blogger.core.api.requests.PostBody
import com.peterchege.blogger.core.api.requests.Viewer
import com.peterchege.blogger.core.api.responses.*
import com.peterchege.blogger.core.room.entities.PostRecord
import com.peterchege.blogger.core.room.entities.PostRecordWithCommentsLikesViews
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    suspend fun getAllPosts():AllPostsResponse

    suspend fun uploadPost(postBody: PostBody): UploadPostResponse

    suspend fun getPostById(postId: String): Post?

    suspend fun deletePostFromApi(postId: String): DeleteResponse

    suspend fun addView(viewer: Viewer): ViewResponse

    suspend fun likePost(likePost: LikePost): LikeResponse

    suspend fun unlikePost(likePost: LikePost): LikeResponse

    suspend fun followUser(followUser: FollowUser): FollowResponse

    suspend fun unfollowUser(followUser: FollowUser): FollowResponse


    suspend fun insertPost(post: Post)

    suspend fun deleteAllPosts()

    suspend fun deletePostById(id: String)

    suspend fun getPostFromRoom(postId: String): PostRecordWithCommentsLikesViews?


    fun getAllPostsFromRoom(): Flow<List<PostRecordWithCommentsLikesViews>>
}