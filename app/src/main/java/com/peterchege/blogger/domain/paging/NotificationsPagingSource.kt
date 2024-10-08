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
import com.peterchege.blogger.core.api.responses.models.Notification
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.domain.repository.NotificationRepository
import timber.log.Timber

@ExperimentalPagingApi
class NotificationsPagingSource(
    private val notificationRepository: NotificationRepository
) : PagingSource<Int, Notification>() {

    val TAG = NotificationsPagingSource::class.java.simpleName

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        val pageNumber = params.key ?: 1
        Timber.tag(TAG).i("Page Number $pageNumber")
        Timber.tag(TAG).i("Params Key ${params.key}")
        val response = notificationRepository.getUserNotifications(page = pageNumber)
        when (response) {
            is NetworkResult.Success -> {
                val prevKey = if (pageNumber == 1) null else pageNumber
                val nextKey = response.data.nextPage
                val pageData = response.data.notifications ?: emptyList()
                if (pageData.isNotEmpty()) {
                    return LoadResult.Page(
                        data = response.data.notifications ?: emptyList(),
                        nextKey = nextKey,
                        prevKey = prevKey,
                    )
                } else {
                    return LoadResult.Error(Throwable(message = "No more results found"))
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

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}