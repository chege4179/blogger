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
import com.peterchege.blogger.core.api.responses.Follower
import com.peterchege.blogger.core.api.responses.Following
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.state.AuthorProfileFollowerFollowingUi
import com.peterchege.blogger.domain.state.AuthorProfileFollowerFollowingUiState
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class AuthorFollowerFollowingScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileUseCase: GetProfileUseCase,

    ) : ViewModel() {
    private val _type = mutableStateOf<String>("")
    val type: State<String> = _type

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user


    private val _uiState = MutableStateFlow<AuthorProfileFollowerFollowingUiState>(
        AuthorProfileFollowerFollowingUiState.Loading)
    val uiState = _uiState.asStateFlow()





    init {
        savedStateHandle.get<String>("username")?.let { username ->
            getProfile(username = username)
        }
        getType()
    }

    private fun getType() {
        savedStateHandle.get<String>("type")?.let {
            _type.value = it

        }
    }

    fun getIsFollowingStatus(username: String): Boolean {
        val followingUsernames = _user.value?.following?.map { it.followedUsername }
        return followingUsernames?.contains(username) ?: false
    }

    private fun getProfile(username: String) {
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