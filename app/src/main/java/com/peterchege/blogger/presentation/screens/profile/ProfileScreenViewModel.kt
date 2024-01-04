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
package com.peterchege.blogger.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.peterchege.blogger.core.analytics.analytics.AnalyticsHelper
import com.peterchege.blogger.core.analytics.analytics.logLogOutEvent
import com.peterchege.blogger.core.api.requests.LogoutUser
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.paging.ProfileScreenPostsPagingSource
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.NetworkInfoRepository
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.domain.repository.ProfileRepository
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import com.peterchege.blogger.domain.use_case.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


sealed interface ProfileScreenUiState {

    object UserNotLoggedIn : ProfileScreenUiState

    object Loading : ProfileScreenUiState

    data class Success(
        val posts: Flow<PagingData<Post>>,
        val user: User
    ) : ProfileScreenUiState

    data class Error(val message: String) : ProfileScreenUiState

    object Empty : ProfileScreenUiState
}


@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val profileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val analyticsHelper: AnalyticsHelper,
    private val networkInfoRepository: NetworkInfoRepository,
) : ViewModel() {

    val networkStatus = networkInfoRepository.networkStatus
        .stateIn(
            scope = viewModelScope,
            initialValue = NetworkStatus.Unknown,
            started = SharingStarted.WhileSubscribed(5000L)
        )

    private val isUserLoggedIn = authRepository.isUserLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    val uiState = combine(authUser, isUserLoggedIn) { user, loggedIn ->
        if (loggedIn) {
            val response = profileUseCase(userId = user?.userId ?: "").last()
            when (response) {
                is Resource.Success -> {
                    // remove non null assertions
                    ProfileScreenUiState.Success(
                        posts = getPaginatedPostsByUserId(userId = user?.userId?:""),
                        user = response.data!!.user,
                    )
                }

                is Resource.Error -> {
                    ProfileScreenUiState.Error(message = "Error")
                }

                is Resource.Loading -> {
                    ProfileScreenUiState.Loading
                }
            }
        } else {
            ProfileScreenUiState.UserNotLoggedIn
        }

    }
        .onStart { ProfileScreenUiState.Loading }
        .catch { ProfileScreenUiState.Error(message = "An error occurred") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ProfileScreenUiState.Loading
        )

    @OptIn(ExperimentalPagingApi::class)
    fun getPaginatedPostsByUserId(userId: String): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ProfileScreenPostsPagingSource(
                    profileRepository = profileRepository,
                    userId = userId
                )
            }
        ).flow
    }


    fun logoutUser(navigateToLoginScreen: () -> Unit, user: User) {
        // fix here
        val username = user.username
        val id = user.userId
        val token = ""
        val logoutUser = LogoutUser(
            deviceToken = token,
            userId = id
        )
        logoutUseCase(logoutUser).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    analyticsHelper.logLogOutEvent(username = username)
                    authRepository.unsetLoggedInUser()
                    navigateToLoginScreen()
                }

                is Resource.Loading -> {

                }

                is Resource.Error -> {

                }
            }

        }.launchIn(viewModelScope)


    }

}