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
package com.peterchege.blogger.data.remote.posts

import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.api.requests.FollowUser
import com.peterchege.blogger.core.api.requests.LikePost
import com.peterchege.blogger.core.api.requests.PostBody
import com.peterchege.blogger.core.api.requests.Viewer
import com.peterchege.blogger.core.api.responses.AllPostsResponse
import com.peterchege.blogger.core.api.responses.DeleteResponse
import com.peterchege.blogger.core.api.responses.FollowResponse
import com.peterchege.blogger.core.api.responses.LikeResponse
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.UploadPostResponse
import com.peterchege.blogger.core.api.responses.ViewResponse
import com.peterchege.blogger.core.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemotePostsDataSourceImpl @Inject constructor(
    private val api:BloggerApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher

):RemotePostsDataSource {
    override suspend fun getAllPosts(): AllPostsResponse {
        return api.getAllPosts()
    }

    override suspend fun uploadPost(postBody: PostBody): UploadPostResponse {
        return api.uploadPost(postBody = postBody)
    }
    override suspend fun getPostById(postId: String): Post? {
        return api.getPostById(postId).post
    }

    override suspend fun deletePostFromApi(postId: String): DeleteResponse {
        return api.getDeletePostById(postId)
    }

    override suspend fun addView(viewer: Viewer): ViewResponse {
        return api.addView(viewer = viewer)
    }

    override suspend fun likePost(likePost: LikePost): LikeResponse = withContext(context = ioDispatcher){
        return@withContext api.likePost(likePost = likePost)
    }



    override suspend fun unlikePost(likePost: LikePost): LikeResponse {
        return api.unlikePost(likePost = likePost)
    }

    override suspend fun followUser(followUser: FollowUser): FollowResponse {
        return api.followUser(followUser = followUser)
    }

    override suspend fun unfollowUser(followUser: FollowUser): FollowResponse {
        return api.unfollowUser(followUser = followUser)
    }

}