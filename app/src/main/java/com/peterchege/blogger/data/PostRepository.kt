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
package com.peterchege.blogger.data


import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.api.requests.FollowUser
import com.peterchege.blogger.core.api.requests.LikePost
import com.peterchege.blogger.core.api.requests.Viewer
import com.peterchege.blogger.core.api.responses.*
import com.peterchege.blogger.core.room.database.BloggerDatabase
import com.peterchege.blogger.core.room.entities.PostRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val api: BloggerApi,
    private val db: BloggerDatabase
) {
    suspend fun getPostById(postId: String): Post? {
        return api.getPostById(postId).post
    }

    suspend fun deletePostFromApi(postId: String): DeleteResponse {
        return api.getDeletePostById(postId)
    }

    suspend fun addView(viewer: Viewer): ViewResponse {
        return api.addView(viewer = viewer)
    }

    suspend fun likePost(likePost: LikePost): LikeResponse {
        return api.likePost(likePost = likePost)
    }

    suspend fun unlikePost(likePost: LikePost): LikeResponse {
        return api.unlikePost(likePost = likePost)
    }

    suspend fun followUser(followUser: FollowUser): FollowResponse {
        return api.followUser(followUser = followUser)
    }

    suspend fun unfollowUser(followUser: FollowUser): FollowResponse {
        return api.unfollowUser(followUser = followUser)
    }

    suspend fun insertPost(post: PostRecord) {
        return db.postDao.insertPost(post)

    }

    suspend fun deleteAllPosts() {
        return db.postDao.deleteAllPosts()

    }

    suspend fun deletePostById(id: String) {
        return db.postDao.deletePostById(id)
    }

    suspend fun getPostFromRoom(postId: String): PostRecord? {
        return db.postDao.getPostById(postId)
    }

    fun getAllPostsFromRoom(): Flow<List<PostRecord>> {
        return db.postDao.getPosts()
    }
}