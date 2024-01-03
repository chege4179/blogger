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
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.PostRepository
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthorProfileScreenUiState {

    object Loading : AuthorProfileScreenUiState

    data class Success(
        val posts: List<Post>,
        val user: User,
        val isUserLoggedIn: Boolean
    ) : AuthorProfileScreenUiState

    data class Error(val message: String) : AuthorProfileScreenUiState


}


@HiltViewModel
class AuthorProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,

) : ViewModel() {

    private val username =
        savedStateHandle.getStateFlow<String>(key = "username", initialValue = "")

    private val isUserLoggedIn = authRepository.isUserLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false,
        )

    val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null,
        )


    val uiState = combine(
        profileUseCase(userId = ""),
        isUserLoggedIn,
    ) { result, loggedIn ->
        when (result) {
            is Resource.Success -> {
                AuthorProfileScreenUiState.Success(
                    posts = result.data?.posts ?: emptyList(),
                    user = result.data!!.user,
                    isUserLoggedIn = loggedIn
                )
            }

            is Resource.Error -> {
                AuthorProfileScreenUiState.Error(
                    message = result.message ?: "An unexpected error occurred"
                )
            }

            is Resource.Loading -> {
                AuthorProfileScreenUiState.Loading
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AuthorProfileScreenUiState.Loading
    )


    fun followUser(userToBeFollowed: User,userFollowing:User) {
        viewModelScope.launch {
//            val followResponse = repository.followUser(
//                FollowUser(
//                    followerUsername = userToBeFollowed.username,
//                    followerFullname = userToBeFollowed.fullname,
//                    followerId = userToBeFollowed._id,
//                    followedUsername = username.value,
//                )
//            )
//            when (followResponse) {
//                is NetworkResult.Success -> {
//                    if (followResponse.data.success) {
//                        authRepository.addUserFollowing(
//                            Following(
//                                followedUsername = userFollowing.username,
//                                followedFullname = userFollowing.fullname,
//                                followedId =userFollowing._id,
//                            )
//                        )
//                    }
//                }
//
//                is NetworkResult.Error -> {
//
//                }
//
//                is NetworkResult.Exception -> {
//
//                }
//            }

        }
    }

    fun unfollowUser(userToBeUnfollowed:User,userUnfollowing:User) {
        viewModelScope.launch {
//            val followResponse = repository.unfollowUser(
//                FollowUser(
//                    followerUsername = userToBeUnfollowed.username,
//                    followerFullname = userToBeUnfollowed.fullname,
//                    followerId = userToBeUnfollowed._id,
//                    followedUsername = username.value,
//                )
//            )
//            when (followResponse) {
//                is NetworkResult.Success -> {
//                    if (followResponse.data.success) {
//                        authRepository.addUserFollowing(
//                            Following(
//                                followedUsername = userUnfollowing.username,
//                                followedFullname = userUnfollowing.fullname,
//                                followedId =userUnfollowing._id,
//                            )
//                        )
//                    }
//                }
//                is NetworkResult.Error -> {
//
//                }
//
//                is NetworkResult.Exception -> {
//
//                }
//            }

        }
    }


}