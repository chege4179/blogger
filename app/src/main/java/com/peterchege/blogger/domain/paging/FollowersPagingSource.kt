/*
 * Copyright 2024 Blogger
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
package com.peterchege.blogger.domain.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.peterchege.blogger.core.api.responses.models.FollowerUser
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.domain.repository.ProfileRepository

@ExperimentalPagingApi
class FollowersPagingSource (
    val userId:String,
    private val profileRepository: ProfileRepository,
): PagingSource<Int, FollowerUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FollowerUser> {
        val pageNumber = params.key ?: 1
        val response = profileRepository.getFollowers(limit = 30, page = pageNumber, userId = userId)
        when (response) {
            is NetworkResult.Success -> {
                val prevKey = if (pageNumber == 1) null else pageNumber
                val nextKey = response.data.nextPage
                val pageData = response.data.followers ?: emptyList()
                if (pageData.isNotEmpty()){
                    return LoadResult.Page(
                        data = response.data.followers ?: emptyList(),
                        nextKey = nextKey,
                        prevKey = prevKey,
                    )
                }else{
                    return LoadResult.Error(Throwable(message = "No more followers"))
                }

            }

            is NetworkResult.Error -> {
                return LoadResult.Error(Throwable(message = response.message))
            }

            is NetworkResult.Exception -> {
                return LoadResult.Error(Throwable(message = response.e.message))
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FollowerUser>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}