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
package com.peterchege.blogger.presentation.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.analytics.analytics.AnalyticsHelper
import com.peterchege.blogger.core.analytics.analytics.logSignUpEvent
import com.peterchege.blogger.core.api.requests.SignUpUser
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

data class SignUpFormState(
    val username: String = "",
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,

    )

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val signUpRepository: AuthRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpFormState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

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

    fun onChangePasswordConfirm(text: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = text)

    }

    fun onChangeEmail(text: String) {
        _uiState.value = _uiState.value.copy(email = text)

    }

    fun onChangeFullName(text: String) {
        _uiState.value = _uiState.value.copy(fullName = text)

    }

    fun signUpUser(navigateToLoginScreen:() -> Unit,) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val confirmPassword = _uiState.value.confirmPassword
            val password = _uiState.value.password

            if (confirmPassword.trim() != password.trim()) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _eventFlow.emit(UiEvent.ShowSnackbar(message = "Passwords do not match"))

            } else {
                val signUpUser = SignUpUser(
                    username = _uiState.value.username.trim(),
                    fullName = _uiState.value.fullName.trim(),
                    password = _uiState.value.password.trim(),
                    email = _uiState.value.email.trim()
                )
                val signUpResponse = signUpRepository.signUpUser(signUpUser = signUpUser)
                when(signUpResponse){
                    is NetworkResult.Success -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = signUpResponse.data.msg))
                        if (signUpResponse.data.success) {
                            analyticsHelper.logSignUpEvent(email = _uiState.value.email)
                            navigateToLoginScreen()

                        }
                    }
                    is NetworkResult.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Server error...Please try again later"))
                    }
                    is NetworkResult.Exception -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Could not connect to the server...Please try again later"))
                    }
                }
            }
        }
    }
}