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
import com.peterchege.blogger.core.api.requests.Viewer
import com.peterchege.blogger.core.api.responses.responses.AllPostsResponse
import com.peterchege.blogger.core.api.responses.responses.DeleteResponse
import com.peterchege.blogger.core.api.responses.responses.FollowResponse
import com.peterchege.blogger.core.api.responses.responses.LikeResponse
import com.peterchege.blogger.core.api.responses.responses.PostResponse
import com.peterchege.blogger.core.api.responses.responses.SearchPostResponse
import com.peterchege.blogger.core.api.responses.responses.UploadPostResponse
import com.peterchege.blogger.core.api.responses.responses.ViewResponse
import com.peterchege.blogger.core.api.safeApiCall
import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.core.util.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import javax.inject.Inject

class RemotePostsDataSourceImpl @Inject constructor(
    private val api:BloggerApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher

):RemotePostsDataSource {
    override suspend fun getAllPosts(): NetworkResult<AllPostsResponse> {
        return safeApiCall { api.getAllPosts() }
    }

    override suspend fun uploadPost(body: RequestBody): NetworkResult<UploadPostResponse> {
        return safeApiCall{ api.createPost(body = body) }
    }
    override suspend fun getPostById(postId: String): NetworkResult<PostResponse> {
        return safeApiCall { api.getPostById(postId) }
    }

    override suspend fun deletePostFromApi(postId: String): NetworkResult<DeleteResponse> {
        return safeApiCall { api.getDeletePostById(postId) }
    }

    override suspend fun searchPosts(searchTerm: String): NetworkResult<SearchPostResponse> {
        return safeApiCall { api.searchPost(searchTerm = searchTerm) }
    }

    override suspend fun addView(viewer: Viewer): NetworkResult<ViewResponse> {
        return safeApiCall { api.addView(viewer = viewer) }
    }

    override suspend fun likePost(likePost: LikePost): NetworkResult<LikeResponse> =
        withContext(context = ioDispatcher){
        return@withContext safeApiCall { api.likePost(likePost = likePost) }
    }



    override suspend fun unlikePost(likePost: LikePost): NetworkResult<LikeResponse> {
        return safeApiCall { api.unlikePost(likePost = likePost) }
    }

    override suspend fun followUser(followUser: FollowUser): NetworkResult<FollowResponse> {
        return safeApiCall { api.followUser(followUser = followUser) }
    }

    override suspend fun unfollowUser(followUser: FollowUser): NetworkResult<FollowResponse> {
        return safeApiCall { api.unfollowUser(followUser = followUser) }
    }

}