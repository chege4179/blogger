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
package com.peterchege.blogger.presentation.screens.author_profile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.requests.FollowUser
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.PostRepository
import com.peterchege.blogger.domain.state.AuthorProfileScreenUi
import com.peterchege.blogger.domain.state.AuthorProfileScreenUiState
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import com.skydoves.sealedx.core.Extensive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject



@HiltViewModel
class AuthorProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,
    private val repository: PostRepository,

    ) : ViewModel() {


    private val _uiState = MutableStateFlow<AuthorProfileScreenUiState>(AuthorProfileScreenUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var _isFollowing = mutableStateOf(false)
    var isFollowing: State<Boolean> = _isFollowing


    val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null,
        )



    init {
        getProfile()
    }



    fun followUser() {
        try {
//            val username = _user.value?.username ?: ""
//            val fullname = _user.value?.fullname ?: ""
//            val userId = _user.value?._id ?: ""
//            viewModelScope.launch {
//                val followResponse = repository.followUser(
//                    FollowUser(
//                        followerUsername = username,
//                        followerFullname = fullname,
//                        followerId = userId,
//                        followedUsername = _state.value.user!!.username,
//                    )
//                )
//                if (followResponse.success) {
//                    _isFollowing.value = true
//                }
//            }

        } catch (e: HttpException) {

        } catch (e: IOException) {

        }
    }

    fun unfollowUser() {
        try {
//            val username = _user.value?.username ?: ""
//            val fullname = _user.value?.fullname ?: ""
//            val userId = _user.value?._id ?: ""
            viewModelScope.launch {
//                val followResponse = repository.unfollowUser(
//                    FollowUser(
//                        followerUsername = username,
//                        followerFullname = fullname,
//                        followerId = userId,
//                        followedUsername = _state.value.user!!.username,
//                    )
//                )
//                if (followResponse.success) {
//                    _isFollowing.value = false
//                }
            }

        } catch (e: HttpException) {
            Log.e("http error", e.localizedMessage ?: "A http error occurred")
        } catch (e: IOException) {
            Log.e("io error", e.localizedMessage ?: "An io error occurred")
        }

    }


    private fun getProfile() {
        savedStateHandle.get<String>("username")?.let { username ->
            profileUseCase(username = username).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.value = AuthorProfileScreenUiState.Success(
                            data = AuthorProfileScreenUi(
                                posts = result.data?.posts ?: emptyList(),
                                user = result.data?.user
                            )
                        )


                    }
                    is Resource.Error -> {
                        _uiState.value = AuthorProfileScreenUiState.Error(
                            message = result.message ?: "An unexpected error occurred")
                    }
                    is Resource.Loading -> {

                    }
                }

            }.launchIn(viewModelScope)
        }


    }

}