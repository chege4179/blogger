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
package com.peterchege.blogger.presentation.screens.dashboard.feed_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.NetworkInfoRepository
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.domain.state.FeedScreenUi
import com.peterchege.blogger.domain.state.FeedScreenUiState
import com.peterchege.blogger.domain.use_case.GetFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeedUseCase: GetFeedUseCase,
    private val authRepository: AuthRepository,
    private val networkInfoRepository: NetworkInfoRepository,
    private val api: BloggerApi
) : ViewModel() {

    val networkStatus = networkInfoRepository.networkStatus
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NetworkStatus.Unknown
        )

    val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    private var _user = MutableStateFlow<User?>(null)
    var user: StateFlow<User?> = _user

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()



    private val _uiState = MutableStateFlow<FeedScreenUiState>(FeedScreenUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getFeedPosts()
    }


    fun getFeedPosts() {
        getFeedUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data == null){
                        _uiState.value = FeedScreenUiState.Error(message = "An unexpected error has occurred")
                    }else{
                        _uiState.value = FeedScreenUiState.Success(data = FeedScreenUi(posts = result.data))
                    }
                }
                is Resource.Error -> {
                    _uiState.value = FeedScreenUiState.Error(message = result.message ?: "An unexpected error occurred")

                }
                is Resource.Loading -> {
                }
            }
        }.launchIn(viewModelScope)
    }
}