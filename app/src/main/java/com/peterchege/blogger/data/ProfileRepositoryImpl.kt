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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.responses.GetFollowersResponse
import com.peterchege.blogger.core.api.responses.responses.GetPostsByUserIdResponse
import com.peterchege.blogger.core.api.responses.responses.GetUserLikeResponse
import com.peterchege.blogger.core.api.responses.responses.ProfileResponse
import com.peterchege.blogger.core.api.safeApiCall
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: BloggerApi,
    ):ProfileRepository {
    override suspend fun getProfile(userId: String): NetworkResult<ProfileResponse> {
        return safeApiCall{ api.getUserProfile(userId = userId) }
    }

    override suspend fun getPostsByUserId(userId: String,page:Int): NetworkResult<GetPostsByUserIdResponse> {
        return safeApiCall { api.getPostByUserId(userId = userId,page = page) }
    }

    override suspend fun getFollowers(
        page: Int,
        limit: Int,
        userId: String
    ): NetworkResult<GetFollowersResponse> {
        return safeApiCall { api.getUserFollowers(page = page,userId = userId) }
    }

    override suspend fun getUserLikes(userId: String): NetworkResult<GetUserLikeResponse> {
        return safeApiCall { api.getUserLikes(userId) }
    }

}