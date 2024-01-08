/*
 * Copyright 2024 Blogger
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
package com.peterchege.blogger.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.data.local.posts.likes.LikesLocalDataSource
import com.peterchege.blogger.data.local.users.FollowersLocalDataSource
import com.peterchege.blogger.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingScreenUiState(
    val isSignOutDialogOpen:Boolean = false,
    val isThemeDialogOpen:Boolean = false,

)
@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val followersLocalDataSource: FollowersLocalDataSource,
    private val likesLocalDataSource: LikesLocalDataSource,
) : ViewModel(){

    private val _uiState = MutableStateFlow(SettingScreenUiState())
    val uiState = _uiState.asStateFlow()


    fun toggleSignOutDialog(){
        val currentState = _uiState.value.isSignOutDialogOpen
        _uiState.value = _uiState.value.copy(isSignOutDialogOpen = !currentState)

    }

    fun toggleThemeDialog(){
        val currentState = _uiState.value.isThemeDialogOpen
        _uiState.value = _uiState.value.copy(isThemeDialogOpen = !currentState)
    }

    fun signOutUser(){
        viewModelScope.launch {
            authRepository.unsetLoggedInUser()
            likesLocalDataSource.deleteAllLikes()
            followersLocalDataSource.deleteAllFollowers()
        }
    }



}