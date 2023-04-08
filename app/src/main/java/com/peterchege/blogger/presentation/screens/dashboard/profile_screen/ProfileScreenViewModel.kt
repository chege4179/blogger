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

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.peterchege.blogger.core.api.requests.LogoutUser
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import com.peterchege.blogger.domain.use_case.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileScreenViewModel @Inject constructor(

    private val logoutUseCase: LogoutUseCase,
    private val profileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {




    private var _isLoading = mutableStateOf(false)
    var isLoading: State<Boolean> = _isLoading

    private var _openDialogState = mutableStateOf(false)
    var openDialogState: State<Boolean> = _openDialogState

    private var _success = mutableStateOf(false)
    var success: State<Boolean> = _success

    private var _isError = mutableStateOf(false)
    var isError: State<Boolean> = _isError

    private var _msg = mutableStateOf("")
    var msg: State<String> = _msg

    private var _posts = mutableStateOf<List<Post>>(emptyList())
    var posts: State<List<Post>> = _posts

    private var _user = MutableStateFlow<User?>(null)
    var user: StateFlow<User?> = _user



    private fun loadUser(){
        viewModelScope.launch {
            authRepository.getLoggedInUser().collectLatest {
                _user.value = it
            }
        }
    }
    init {
        loadUser()
        getProfile()
    }

    fun onDialogConfirm(scaffoldState: ScaffoldState, postTitle: String, postId: String) {
        _openDialogState.value = false
    }

    fun onDialogOpen(postTitle: String) {
        _openDialogState.value = true
    }

    fun onDialogDismiss() {
        _openDialogState.value = false
    }

    fun logoutUser(navController: NavController) {
        val sharedPref: SharedPreferences? = null

        val username = _user.value?.username ?: ""
        val id = _user.value?._id ?: ""
        val token = ""

        val logoutUser = LogoutUser(
            username = username,
            token = token,
            id = id
        )

        logoutUseCase(logoutUser).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    authRepository.unsetLoggedInUser()
                    navController.navigate(Screens.LOGIN_SCREEN)
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
            }

        }.launchIn(viewModelScope)


    }


    private fun getProfile() {
        viewModelScope.launch {
            profileUseCase(username = _user.value?.username ?: "").onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _isLoading.value = false
                        _msg.value = result.data!!.msg
                        _success.value = result.data.success
                        _posts.value = result.data.posts
                        _user.value = result.data.user
                        _isError.value = false

                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        result.message?.let {
                            _isLoading.value = false
                            _msg.value = it
                            _success.value = false
                            _posts.value = emptyList()
                            _isError.value = true
                        }
                    }
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }
                }
            }.launchIn(this)
        }
    }
}