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
package com.peterchege.blogger.presentation.screens.author

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.peterchege.blogger.core.api.responses.models.FollowerUser
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.data.local.users.follower.FollowersLocalDataSource
import com.peterchege.blogger.data.local.users.following.FollowingLocalDataSource
import com.peterchege.blogger.domain.paging.FollowersPagingSource
import com.peterchege.blogger.domain.paging.FollowingPagingSource
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.ProfileRepository
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface AuthorProfileFollowerFollowingScreenUiState {

    object Loading : AuthorProfileFollowerFollowingScreenUiState

    data class Followers(
        val followers: Flow<PagingData<FollowerUser>>,
        val isUserLoggedIn: Boolean,
        val type: String,
    ):AuthorProfileFollowerFollowingScreenUiState
    data class Following(
        val following: Flow<PagingData<FollowerUser>>,
        val isUserLoggedIn: Boolean,
        val type: String,
    ) : AuthorProfileFollowerFollowingScreenUiState

    data class Error(val message: String) : AuthorProfileFollowerFollowingScreenUiState


}

@HiltViewModel
class AuthorFollowerFollowingScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val followingLocalDataSource: FollowingLocalDataSource,
    private val followersLocalDataSource: FollowersLocalDataSource,

    ) : ViewModel() {
    private val type = savedStateHandle.getStateFlow<String>(key = "type", initialValue = "")
    private val userId = savedStateHandle.getStateFlow<String>(key = "userId", initialValue = "")

    val followingUserIds = followingLocalDataSource.getAllAuthUserFollowingsIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val followerUserIds = followersLocalDataSource.getAllAuthUserFollowerIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )


    private val isUserLoggedIn = authRepository.isUserLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    private val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )


    val uiState = combine(
        profileUseCase(userId = userId.value),
        isUserLoggedIn,
        authUser,
        type,
    ) { result, loggedIn, user, type ->
        when (result) {
            is Resource.Success -> {
                if (type == Constants.FOLLOWER){
                    AuthorProfileFollowerFollowingScreenUiState.Followers(
                        followers = getUserFollowersById(userId.value),
                        type = type,
                        isUserLoggedIn = loggedIn
                    )
                }else{
                    AuthorProfileFollowerFollowingScreenUiState.Following(
                        following = getUserFollowingsById(userId.value),
                        type = type,
                        isUserLoggedIn = loggedIn
                    )
                }

            }

            is Resource.Error -> {
                AuthorProfileFollowerFollowingScreenUiState.Error(
                    message = result.message ?: "An unexpected error occurred"
                )
            }

            is Resource.Loading -> {
                AuthorProfileFollowerFollowingScreenUiState.Loading
            }
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AuthorProfileFollowerFollowingScreenUiState.Loading
    )


    @OptIn(ExperimentalPagingApi::class)
    private fun getUserFollowersById(userId: String): Flow<PagingData<FollowerUser>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FollowersPagingSource(
                    profileRepository = profileRepository,
                    userId = userId
                )
            }
        ).flow.cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun getUserFollowingsById(userId: String): Flow<PagingData<FollowerUser>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FollowingPagingSource(
                    profileRepository = profileRepository,
                    userId = userId
                )
            }
        ).flow.cachedIn(viewModelScope)
    }
}