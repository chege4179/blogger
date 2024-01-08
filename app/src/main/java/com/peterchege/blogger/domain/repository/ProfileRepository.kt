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

import androidx.paging.PagingData
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.responses.GetFollowersResponse
import com.peterchege.blogger.core.api.responses.responses.GetFollowingResponse
import com.peterchege.blogger.core.api.responses.responses.GetPostsByUserIdResponse
import com.peterchege.blogger.core.api.responses.responses.GetUserLikeResponse
import com.peterchege.blogger.core.api.responses.responses.ProfileResponse
import com.peterchege.blogger.core.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getProfile(userId: String):NetworkResult<ProfileResponse>

    suspend fun getPostsByUserId(userId: String,page:Int):NetworkResult<GetPostsByUserIdResponse>


    suspend fun getFollowers(page: Int, limit:Int,userId: String):NetworkResult<GetFollowersResponse>

    suspend fun getFollowing(page: Int, limit:Int,userId: String):NetworkResult<GetFollowingResponse>

    suspend fun getUserLikes(userId: String):NetworkResult<GetUserLikeResponse>
}