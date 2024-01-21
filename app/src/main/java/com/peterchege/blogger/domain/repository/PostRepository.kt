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
import com.peterchege.blogger.core.api.requests.UpdatePost
import com.peterchege.blogger.core.api.requests.Viewer
import com.peterchege.blogger.core.api.responses.responses.DeleteResponse
import com.peterchege.blogger.core.api.responses.responses.FollowResponse
import com.peterchege.blogger.core.api.responses.responses.LikeResponse
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.responses.SearchPostResponse
import com.peterchege.blogger.core.api.responses.responses.UnLikeResponse
import com.peterchege.blogger.core.api.responses.responses.UpdatePostResponse
import com.peterchege.blogger.core.api.responses.responses.UploadPostResponse
import com.peterchege.blogger.core.api.responses.responses.ViewResponse
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.domain.models.PostUI
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface PostRepository {

    fun getAllPosts():Flow<List<PostUI>>

    suspend fun uploadPost(body: RequestBody):NetworkResult<UploadPostResponse>

    fun getPostById(postId: String):Flow<Post?>

    suspend fun syncFeed()

    suspend fun deletePostFromApi(postId: String):NetworkResult<DeleteResponse>

    suspend fun addView(viewer: Viewer):NetworkResult<ViewResponse>

    suspend fun likePost(likePost: LikePost):NetworkResult<LikeResponse>

    suspend fun unlikePost(likePost: LikePost):NetworkResult<UnLikeResponse>


    suspend fun insertSavedPost(post: Post)

    suspend fun deleteAllSavedPosts()

    suspend fun deleteSavedPostById(id: String)

    suspend fun getSavedPost(postId: String): Flow<Post?>

    suspend fun updatePost(updatePost: UpdatePost):NetworkResult<UpdatePostResponse>

    fun getSavedPostIds():Flow<List<String>>


    fun getAllSavedPosts(): Flow<List<Post>>
}