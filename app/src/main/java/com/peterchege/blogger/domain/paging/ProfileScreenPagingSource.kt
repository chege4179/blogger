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
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.domain.repository.ProfileRepository
import javax.inject.Inject


@ExperimentalPagingApi
class ProfileScreenPostsPagingSource (
    private val profileRepository: ProfileRepository,
    val userId: String,
): PagingSource<Int, Post>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,Post> {
        val pageNumber = params.key ?: 1
        val response = profileRepository.getPostsByUserId(userId = userId,page = pageNumber)
        when(response){
            is NetworkResult.Success -> {
                val prevKey = if (pageNumber > 0) pageNumber - 1 else null
                val nextKey = response.data.nextPage
                return LoadResult.Page(
                    data = response.data.posts ?: emptyList(),
                    nextKey = nextKey,
                    prevKey = prevKey,
                )
            }
            is NetworkResult.Error -> {
                return LoadResult.Error(Throwable(message = response.message))
            }
            is NetworkResult.Exception -> {
                return LoadResult.Error(Throwable(message = response.e.message))
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}

