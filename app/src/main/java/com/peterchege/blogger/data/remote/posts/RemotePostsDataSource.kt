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
import com.peterchege.blogger.core.util.NetworkResult
import okhttp3.RequestBody

interface RemotePostsDataSource {

    suspend fun getAllPosts(): NetworkResult<AllPostsResponse>

    suspend fun uploadPost(body: RequestBody):NetworkResult<UploadPostResponse>

    suspend fun getPostById(postId: String): NetworkResult<PostResponse>

    suspend fun deletePostFromApi(postId: String): NetworkResult<DeleteResponse>

    suspend fun searchPosts(searchTerm:String):NetworkResult<SearchPostResponse>

    suspend fun addView(viewer: Viewer): NetworkResult<ViewResponse>

    suspend fun likePost(likePost: LikePost):NetworkResult<LikeResponse>


    suspend fun unlikePost(likePost: LikePost):NetworkResult<LikeResponse>

    suspend fun followUser(followUser: FollowUser):NetworkResult<FollowResponse>

    suspend fun unfollowUser(followUser: FollowUser):NetworkResult<FollowResponse>
}