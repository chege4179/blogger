package com.peterchege.blogger.domain.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.peterchege.blogger.core.api.responses.models.Notification
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.domain.repository.NotificationRepository

@ExperimentalPagingApi
class NotificationsPagingSource (
    val userId:String,
    private val notificationRepository: NotificationRepository
): PagingSource<Int, Notification>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        val pageNumber = params.key ?: 1
        val response = notificationRepository.getUserNotifications(page = pageNumber, userId = userId)
        when (response) {
            is NetworkResult.Success -> {
                val prevKey = if (pageNumber > 0) pageNumber - 1 else null
                val nextKey = response.data.nextPage
                val pageData = response.data.notifications ?: emptyList()
                if (pageData.isNotEmpty()){
                    return LoadResult.Page(
                        data = response.data.notifications ?: emptyList(),
                        nextKey = nextKey,
                        prevKey = prevKey,
                    )
                }else{
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