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
package com.peterchege.blogger.presentation.screens.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.NetworkInfoRepository
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SearchScreenUiState {
    object Idle : SearchScreenUiState
    object Searching : SearchScreenUiState

    data class ResultsFound(
        val posts: List<Post>,
        val users: List<User>,
    ) : SearchScreenUiState

    data class Error(val message: String) : SearchScreenUiState

}


@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository,
    private val api: BloggerApi,
    private val savedStateHandle: SavedStateHandle,
    private val networkInfoRepository: NetworkInfoRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher

    ) : ViewModel() {

    val networkStatus = networkInfoRepository.networkStatus
        .onStart { emit(NetworkStatus.Unknown) }
        .catch { emit(NetworkStatus.Unknown) }
        .flowOn(ioDispatcher)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NetworkStatus.Unknown
        )
    private val SEARCH_QUERY = "searchQuery"

    val searchQuery = savedStateHandle.getStateFlow(SEARCH_QUERY, "")

    private val _uiState = MutableStateFlow<SearchScreenUiState>(SearchScreenUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    private var _user = MutableStateFlow<User?>(null)
    var user: StateFlow<User?> = _user

    fun onChangeSearchTerm(searchTerm: String) {
        _uiState.value = SearchScreenUiState.Searching
        savedStateHandle[SEARCH_QUERY] = searchTerm
        if (searchTerm.length > 3) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                val response = postRepository.searchPosts(searchTerm = searchTerm)
                when (response) {
                    is NetworkResult.Success -> {
                        _uiState.value  = SearchScreenUiState.ResultsFound(
                            posts = response.data.posts,
                            users = response.data.users
                        )
                    }

                    is NetworkResult.Error -> {
                        _uiState.value = SearchScreenUiState.Error(message = "An unexpected error has occurred")
                    }
                    is NetworkResult.Exception -> {
                        _uiState.value = SearchScreenUiState.Error(message = "An unexpected error has occurred")
                    }
                }
            }
        } else if (searchTerm.length < 2) {
            _uiState.value = SearchScreenUiState.Idle
            viewModelScope.launch {
                delay(500L)
            }

        }
    }
}