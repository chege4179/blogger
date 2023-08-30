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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.responses.Follower
import com.peterchege.blogger.core.api.responses.Following
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface AuthorProfileFollowerFollowingScreenUiState {

    object Loading : AuthorProfileFollowerFollowingScreenUiState

    data class Success(
        val following:List<Following>,
        val followers:List<Follower>,
        val isUserLoggedIn:Boolean,
        val type:String,
    ) : AuthorProfileFollowerFollowingScreenUiState

    data class Error(val message: String) : AuthorProfileFollowerFollowingScreenUiState


}





@HiltViewModel
class AuthorFollowerFollowingScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,

    ) : ViewModel() {
    private val type =  savedStateHandle.getStateFlow<String>(key = "type" , initialValue = "")
    private val authorUsername = savedStateHandle.getStateFlow<String>(key = "username", initialValue = "")


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
        profileUseCase(username = authorUsername.value),
        isUserLoggedIn,
        authUser,
        type,
    ){ result, loggedIn, user,type ->
        when (result) {
            is Resource.Success -> {
                AuthorProfileFollowerFollowingScreenUiState.Success(
                    followers = result.data?.user?.followers ?: emptyList(),
                    following = result.data?.user?.following ?: emptyList(),
                    type = type,
                    isUserLoggedIn = loggedIn
                )
            }
            is Resource.Error -> {
                AuthorProfileFollowerFollowingScreenUiState.Error(
                    message = result.message ?: "An unexpected error occurred")
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
}