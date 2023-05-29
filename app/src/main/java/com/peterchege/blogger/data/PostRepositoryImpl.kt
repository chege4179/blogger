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
import com.peterchege.blogger.core.api.requests.PostBody
import com.peterchege.blogger.core.api.requests.Viewer
import com.peterchege.blogger.core.api.responses.*
import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.core.room.database.BloggerDatabase
import com.peterchege.blogger.core.room.entities.PostRecord
import com.peterchege.blogger.core.room.entities.PostRecordWithCommentsLikesViews
import com.peterchege.blogger.data.local.posts.cached_posts.CachedPostsDataSource
import com.peterchege.blogger.data.local.posts.saved_posts.SavedPostsDataSource
import com.peterchege.blogger.data.remote.posts.RemotePostsDataSource
import com.peterchege.blogger.domain.repository.PostRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val cachedPostsDataSource: CachedPostsDataSource,
    private val remotePostsDataSource: RemotePostsDataSource,
    private val savedPostsDataSource: SavedPostsDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
):PostRepository {
    override fun getAllPosts():Flow<List<Post>> {
        return cachedPostsDataSource.getCachedPosts()
    }

    override suspend fun uploadPost(postBody: PostBody): UploadPostResponse {
        return remotePostsDataSource.uploadPost(postBody = postBody)
    }

    override suspend fun getPostById(postId: String): Post? {
        return remotePostsDataSource.getPostById(postId = postId)
    }

    override suspend fun deletePostFromApi(postId: String): DeleteResponse {
        return remotePostsDataSource.deletePostFromApi(postId = postId)
    }

    override suspend fun addView(viewer: Viewer): ViewResponse {
        return remotePostsDataSource.addView(viewer = viewer)
    }

    override suspend fun likePost(likePost: LikePost): LikeResponse {
        return remotePostsDataSource.likePost(likePost = likePost)
    }

    override suspend fun unlikePost(likePost: LikePost): LikeResponse {
        return remotePostsDataSource.unlikePost(likePost = likePost)
    }

    override suspend fun followUser(followUser: FollowUser): FollowResponse {
        return remotePostsDataSource.followUser(followUser = followUser)
    }

    override suspend fun unfollowUser(followUser: FollowUser): FollowResponse {
        return remotePostsDataSource.unfollowUser(followUser = followUser)
    }

    override suspend fun insertSavedPost(post: Post) = withContext(ioDispatcher) {
        savedPostsDataSource.insertPost(post = post)
    }

    override suspend fun deleteAllSavedPosts() {
        withContext(ioDispatcher){
            savedPostsDataSource.deleteAllPosts()
        }
    }

    override suspend fun deleteSavedPostById(id: String) {
        withContext(ioDispatcher){
            savedPostsDataSource.deletePostById(id = id)
        }
    }

    override suspend fun getSavedPost(postId: String): PostRecordWithCommentsLikesViews? {
        return withContext(ioDispatcher){
            savedPostsDataSource.getPostFromRoom(postId = postId)
        }
    }

    override fun getAllSavedPosts(): Flow<List<PostRecordWithCommentsLikesViews>> {
        return savedPostsDataSource.getAllPostsFromRoom()
    }


}