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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.responses.models.User

import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.PostRepository
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface ProfileFollowerFollowingScreenUiState {

    object Loading : ProfileFollowerFollowingScreenUiState

    data class Success(
        val following: List<User>,
        val followers: List<User>,
        val type: String,
    ) : ProfileFollowerFollowingScreenUiState

    data class Error(val message: String) : ProfileFollowerFollowingScreenUiState

}

@HiltViewModel
class ProfileFollowerFollowingScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,
    private val repository: PostRepository,

    ) : ViewModel() {

    // follower or following
    private val typeFlow = savedStateHandle.getStateFlow<String>(key = "type", initialValue = "")
    private val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )


    val uiState = combine(typeFlow, authUser) { type, user ->
        if (user == null) {
            ProfileFollowerFollowingScreenUiState.Error("Info not found")
        } else {
            ProfileFollowerFollowingScreenUiState.Success(
                type = type,
                // TODO Check here
                followers = emptyList(),
                following = emptyList(),
            )
        }
    }
        .onStart { ProfileFollowerFollowingScreenUiState.Loading }
        .catch { ProfileFollowerFollowingScreenUiState.Error("An unexpected error occurred") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ProfileFollowerFollowingScreenUiState.Loading
        )


    fun followUser(followedUsername: String) {
//        viewModelScope.launch {
//            val username = _user.value?.username ?: ""
//            val fullname = _user.value?.fullname ?: ""
//            val userId = _user.value?._id ?: ""
//            val followResponse = repository.followUser(
//                FollowUser(
//                    followerUsername = username,
//                    followerFullname = fullname,
//                    followerId = userId,
//                    followedUsername = followedUsername,
//                )
//            )
//
//        }
    }

    fun unfollowUser(followedUsername: String) {
        viewModelScope.launch {
//            val username = _user.value?.username ?: ""
//            val fullname = _user.value?.fullname ?: ""
//            val userId = _user.value?._id ?: ""
//            val followResponse = repository.unfollowUser(
//                FollowUser(
//                    followerUsername = username,
//                    followerFullname = fullname,
//                    followerId = userId!!,
//                    followedUsername = followedUsername,
//                )
//            )
        }
    }

}