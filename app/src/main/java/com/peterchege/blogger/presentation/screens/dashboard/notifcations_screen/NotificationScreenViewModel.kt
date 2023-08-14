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
package com.peterchege.blogger.presentation.screens.dashboard.notifcations_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.requests.Notification
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.state.NotificationScreenUi
import com.peterchege.blogger.domain.state.NotificationScreenUiState
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<NotificationScreenUiState>(NotificationScreenUiState.Loading)
    val uiState = _uiState.asStateFlow()

    val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    fun getNotifications(username:String) {
        getProfileUseCase(username = username).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _uiState.value = NotificationScreenUiState.Success(
                        data = NotificationScreenUi(
                            notifications = result.data?.user?.notifications ?: emptyList()

                        )
                    )

                }

                is Resource.Loading -> {
                    _uiState.value = NotificationScreenUiState.Loading
                }

                is Resource.Error -> {
                    _uiState.value = NotificationScreenUiState.Error(
                        message = result.message ?: "An unexpected error occurred"
                    )
                }
            }

        }.launchIn(viewModelScope)

    }
}