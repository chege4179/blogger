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

