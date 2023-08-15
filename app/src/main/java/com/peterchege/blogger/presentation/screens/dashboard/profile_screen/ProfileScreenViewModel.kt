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
import com.peterchege.blogger.core.analytics.analytics.AnalyticsHelper
import com.peterchege.blogger.core.analytics.analytics.logLogOutEvent
import com.peterchege.blogger.core.api.requests.LogoutUser
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.domain.models.PostUI
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import com.peterchege.blogger.domain.use_case.LogoutUseCase
import com.peterchege.blogger.presentation.screens.dashboard.feed_screen.FeedScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject



sealed interface ProfileScreenUiState {
    object Loading : ProfileScreenUiState

    data class Success(val posts:List<Post>,val user:User) : ProfileScreenUiState

    data class Error(val message: String) : ProfileScreenUiState

    object Empty : ProfileScreenUiState
}





@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val profileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {
    val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    val uiState = authRepository.getLoggedInUser()
        .map<User?, ProfileScreenUiState> { user ->
            if (user == null){
                ProfileScreenUiState.Empty
            }else{
                val response = profileUseCase(username = user.username).last()
                when(response){
                    is Resource.Success -> {
                        ProfileScreenUiState.Success(
                            posts = response.data?.posts ?: emptyList(),
                            user = response.data?.user ?: user,
                        )
                    }
                    is Resource.Error -> {
                        ProfileScreenUiState.Error(message = "Error")
                    }
                    is Resource.Loading -> {
                        ProfileScreenUiState.Loading
                    }
                }

            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ProfileScreenUiState.Loading
        )

    private var _openDialogState = mutableStateOf(false)
    var openDialogState: State<Boolean> = _openDialogState


    fun onDialogConfirm(scaffoldState: ScaffoldState, postTitle: String, postId: String) {
        _openDialogState.value = false
    }

    fun onDialogOpen(postTitle: String) {
        _openDialogState.value = true
    }

    fun onDialogDismiss() {
        _openDialogState.value = false
    }

    fun logoutUser(navigateToLoginScreen:() -> Unit,user: User) {
        val sharedPref: SharedPreferences? = null

        val username = user.username ?: ""
        val id = user._id ?: ""
        val token = ""

        val logoutUser = LogoutUser(
            username = username,
            token = token,
            id = id
        )

        logoutUseCase(logoutUser).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    analyticsHelper.logLogOutEvent(username = username)
                    authRepository.unsetLoggedInUser()
                    navigateToLoginScreen()
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
            }

        }.launchIn(viewModelScope)


    }

}