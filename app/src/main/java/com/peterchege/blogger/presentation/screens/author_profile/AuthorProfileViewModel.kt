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
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
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


    private var _state = mutableStateOf<ProfileResponseState>(ProfileResponseState())
    var state: State<ProfileResponseState> = _state

    private var _isLoading = mutableStateOf(false)
    var isLoading: State<Boolean> = _isLoading

    private var _isFound = mutableStateOf(false)
    var isFound: State<Boolean> = _isFound

    private var _isFollowing = mutableStateOf(false)
    var isFollowing: State<Boolean> = _isFollowing

    data class ProfileResponseState(
        val msg: String = "",
        val success: Boolean = false,
        val user: User? = null,
        val posts: List<Post> = emptyList()
    )

    init {
        loadUser()
        getProfile()

    }

    private var _user = MutableStateFlow<User?>(null)
    var user: StateFlow<User?> = _user


    private fun loadUser() {
        viewModelScope.launch {
            authRepository.getLoggedInUser().collectLatest {
                _user.value = it
            }
        }
    }


    private fun getFollowingStatus() {
        val followers = _state.value.user?.followers
        val username = _user.value?.username ?: ""
        val followerUsernames = followers?.map { it.followerUsername }
        _isFollowing.value = followerUsernames!!.contains(username)

    }


    fun followUser() {
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
                        followedUsername = _state.value.user!!.username,
                    )
                )
                if (followResponse.success) {
                    _isFollowing.value = true
                }
            }

        } catch (e: HttpException) {

        } catch (e: IOException) {

        }
    }

    fun unfollowUser() {
        try {
            val username = _user.value?.username ?: ""
            val fullname = _user.value?.fullname ?: ""
            val userId = _user.value?._id ?: ""
            viewModelScope.launch {
                val followResponse = repository.unfollowUser(
                    FollowUser(
                        followerUsername = username,
                        followerFullname = fullname,
                        followerId = userId,
                        followedUsername = _state.value.user!!.username,
                    )
                )
                if (followResponse.success) {
                    _isFollowing.value = false
                }
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
                        _isLoading.value = false
                        _isFound.value = true
                        result.data?.user?.let { Log.e("user", it.username) }
                        _state.value = ProfileResponseState(
                            msg = result.data!!.msg,
                            success = result.data.success,
                            posts = result.data.posts,
                            user = result.data.user,
                        )
                        getFollowingStatus()


                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        _isFound.value = false
                    }
                    is Resource.Loading -> {
                        _isLoading.value = true

                    }
                }

            }.launchIn(viewModelScope)
        }


    }

}