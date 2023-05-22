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
package com.peterchege.blogger.presentation.screens.dashboard.profile_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.requests.FollowUser
import com.peterchege.blogger.core.api.responses.Follower
import com.peterchege.blogger.core.api.responses.Following
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.PostRepository
import com.peterchege.blogger.domain.state.AuthorProfileFollowerFollowingUi
import com.peterchege.blogger.domain.state.AuthorProfileFollowerFollowingUiState
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ProfileFollowerFollowingScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,
    private val repository: PostRepository,

    ) : ViewModel() {
    
    private var _type = mutableStateOf<String>("")
    var type: State<String> = _type

    private var _isLoading = mutableStateOf<Boolean>(false)
    var isLoading: State<Boolean> = _isLoading

    private var _user = MutableStateFlow<User?>(null)
    var user: StateFlow<User?> = _user

    private val _uiState = MutableStateFlow<AuthorProfileFollowerFollowingUiState>(
        AuthorProfileFollowerFollowingUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private fun loadUser(){
        viewModelScope.launch {
            authRepository.getLoggedInUser().collectLatest {
                _user.value = it
            }
        }
    }

    init {
        loadUser()
        getType()
        getProfile()
    }

    private fun getType() {
        savedStateHandle.get<String>("type")?.let {
            _type.value = it

        }
    }

    fun followUser(followedUsername: String) {
        try {
            val username = _user.value?.username ?: ""
            val fullname = _user.value?.fullname ?: ""
            val userId = _user.value?._id ?: ""
            viewModelScope.launch {
                val followResponse = repository.followUser(
                    FollowUser(
                        followerUsername = username,
                        followerFullname = fullname,
                        followerId = userId,
                        followedUsername = followedUsername,
                    )
                )

            }

        } catch (e: HttpException) {
            Log.e("http error", e.localizedMessage ?: "A http error occurred")
        } catch (e: IOException) {
            Log.e("io error", e.localizedMessage ?: "An io error occurred")
        }


    }

    fun unfollowUser(followedUsername: String) {
        try {
            val username = _user.value?.username ?: ""
            val fullname = _user.value?.fullname ?: ""
            val userId = _user.value?._id ?: ""
            viewModelScope.launch {
                val followResponse = repository.unfollowUser(
                    FollowUser(
                        followerUsername = username,
                        followerFullname = fullname,
                        followerId = userId!!,
                        followedUsername = followedUsername,
                    )
                )
            }
        } catch (e: HttpException) {
            Log.e("http error", e.localizedMessage ?: "A http error occurred")
        } catch (e: IOException) {
            Log.e("io error", e.localizedMessage ?: "An io error occurred")
        }

    }


    private fun getProfile() {
        _isLoading.value = true
        val username = _user.value?.username ?: ""

        profileUseCase(username = username).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _user.value = result.data?.user
                    _uiState.value = AuthorProfileFollowerFollowingUiState.Success(
                        AuthorProfileFollowerFollowingUi(
                            followers = result.data?.user?.followers ?: emptyList(),
                            following = result.data?.user?.following ?: emptyList()
                        )
                    )
                }
                is Resource.Error -> {
                    _uiState.value = AuthorProfileFollowerFollowingUiState.Error(
                        message = result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _uiState.value = AuthorProfileFollowerFollowingUiState.Loading
                }
            }

        }.launchIn(viewModelScope)

    }
}