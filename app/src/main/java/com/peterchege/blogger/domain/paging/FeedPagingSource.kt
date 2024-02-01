package com.peterchege.blogger.domain.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.peterchege.blogger.domain.models.PostUI
import com.peterchege.blogger.domain.repository.AuthRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FeedPagingSource @Inject constructor(
    private val authRepository: AuthRepository,

):RemoteMediator<Int,PostUI>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, PostUI>): MediatorResult {
        TODO("Not yet implemented")
    }
}