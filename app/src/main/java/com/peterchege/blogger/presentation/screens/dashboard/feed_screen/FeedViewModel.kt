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
import com.peterchege.blogger.domain.repository.AuthRepository
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
    private val api: BloggerApi
) : ViewModel() {

    private var _user = MutableStateFlow<User?>(null)
    var user: StateFlow<User?> = _user



    private fun loadUser(){
        viewModelScope.launch {
            authRepository.getLoggedInUser().collectLatest {
                _user.value = it
            }
        }
    }


    data class FeedListState(
        val msg: String = "",
        val posts: List<Post> = emptyList(),
        val users: List<User> = emptyList(),
        val success: Boolean = false,
        val error: String = "",
        val isLoading: Boolean = false,


        )

    private val _state = mutableStateOf(FeedListState())
    val state: State<FeedListState> = _state

    private val _searchTerm = mutableStateOf("")
    val searchTerm: State<String> = _searchTerm

    private val _isFound = mutableStateOf(true)
    val isFound: State<Boolean> = _isFound

    private val _isError = mutableStateOf(false)
    val isError: State<Boolean> = _isError

    private val _errorMsg = mutableStateOf("")
    val errorMsg: State<String> = _errorMsg

    init {
        loadUser()
        getFeedPosts()
    }




    fun onProfileNavigate(
        username: String,
        bottomNavController: NavController,
        navHostController: NavHostController
    ) {
        val loginUsername = _user.value?.username ?: ""
        if (loginUsername == username) {
            bottomNavController.navigate(Screens.PROFILE_NAVIGATION)
        } else {
            navHostController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/$username")
        }

    }

    private fun getFeedPosts() {
        getFeedUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value =
                        FeedListState(posts = result.data ?: emptyList(), isLoading = false)
                }
                is Resource.Error -> {
                    _state.value = FeedListState(
                        error = result.message ?: "An error occurred",
                        isLoading = false
                    )
                    _isError.value = true
                    _errorMsg.value = result.message ?: "An unexpected error occurred"
                }
                is Resource.Loading -> {
                    _state.value = FeedListState(isLoading = true)

                }
            }
        }.launchIn(viewModelScope)
    }
}