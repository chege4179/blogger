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
package com.peterchege.blogger.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.peterchege.blogger.core.analytics.analytics.AnalyticsHelper
import com.peterchege.blogger.core.analytics.analytics.logLoginEvent
import com.peterchege.blogger.core.api.requests.LoginUser
import com.peterchege.blogger.core.util.*
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.NetworkInfoRepository
import com.peterchege.blogger.domain.repository.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class LoginFormState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
)

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val networkInfoRepository: NetworkInfoRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    val networkStatus = networkInfoRepository.networkStatus
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NetworkStatus.Unknown
        )

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var _uiState = MutableStateFlow(LoginFormState())
    val uiState = _uiState.asStateFlow()


    fun onChangePasswordVisibility() {
        val initialState = _uiState.value.isPasswordVisible
        _uiState.value = _uiState.value.copy(isPasswordVisible = !initialState)
    }

    fun onChangeUsername(text: String) {
        _uiState.value = _uiState.value.copy(username = text)
    }

    fun onChangePassword(text: String) {
        _uiState.value = _uiState.value.copy(password = text)

    }

    fun initiateLogin(navigateToDashBoard:() ->Unit) {
        viewModelScope.launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                _uiState.value = _uiState.value.copy(isLoading = true)
                val username = _uiState.value.username
                val password = _uiState.value.password
                if (username.length < 5 || password.length < 5) {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "Credentials should be at least 5 characters long"))
                } else {
                    val loginUser = LoginUser(
                        username = username.trim(),
                        password = password.trim(),
                        deviceToken = token!!
                    )
                    val response = repository.loginUser(loginUser)
                    when (response) {
                        is NetworkResult.Success -> {
                            _uiState.value = _uiState.value.copy(isLoading = false)
                            if (response.data.success) {
                                analyticsHelper.logLoginEvent(username = _uiState.value.username)
                                response.data.user?.let {
                                    repository.setLoggedInUser(user = it)

                                }
                                navigateToDashBoard()
                            }else{
                                println(response.data.msg)
                                _eventFlow.emit(UiEvent.ShowSnackbar(message = response.data.msg))
                            }
                        }

                        is NetworkResult.Error -> {
                            _uiState.value = _uiState.value.copy(isLoading = false)
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar(
                                    message =  "An unexpected error occurred"
                                )
                            )
                        }

                        is NetworkResult.Exception -> {
                            _uiState.value = _uiState.value.copy(isLoading = false)
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar(
                                    message =  "An unexpected exception occurred"
                                )
                            )
                        }

                    }


                }
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar(message = "Unexpected error "))
            }
        }
    }
}