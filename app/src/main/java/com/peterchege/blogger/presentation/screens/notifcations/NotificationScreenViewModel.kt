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
package com.peterchege.blogger.presentation.screens.notifcations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.requests.Notification
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.repository.AuthRepository

import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

sealed interface NotificationScreenUiState {

    object UserNotLoggedIn: NotificationScreenUiState

    object Loading : NotificationScreenUiState

    data class Success(val notifications:List<Notification>) : NotificationScreenUiState

    data class Error(val message: String) : NotificationScreenUiState


}



@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<NotificationScreenUiState>(NotificationScreenUiState.Loading)
    val uiState = _uiState.asStateFlow()

    val isUserLoggedIn  = authRepository.isUserLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )
    val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    fun getNotifications(username:String,isUserLoggedIn:Boolean) {
        if (isUserLoggedIn){
            getProfileUseCase(userId = username).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.value = NotificationScreenUiState.Success(
                            // TODO : And notification
                            notifications =  emptyList(),
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
        }else{
            _uiState.value = NotificationScreenUiState.UserNotLoggedIn
        }




    }
}