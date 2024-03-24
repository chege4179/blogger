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
import com.peterchege.blogger.core.firebase.analytics.AnalyticsHelper
import com.peterchege.blogger.core.firebase.analytics.logSignUpEvent
import com.peterchege.blogger.core.api.requests.OtpTriggerBody
import com.peterchege.blogger.core.api.requests.SignUpUser
import com.peterchege.blogger.core.api.requests.ValidateOtpBody
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignUpFormState(
    val username: String = "",
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,

    val isOtpBottomSheetVisible: Boolean = false,
    val otpInput: String = "",


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
        _uiState.update {
            it.copy(isPasswordVisible = !initialState)
        }
    }

    fun onChangeUsername(text: String) {
        _uiState.update {
            it.copy(username = text)
        }
    }

    fun onChangePassword(text: String) {
        _uiState.update {
            it.copy(password = text)
        }
    }

    fun onChangePasswordConfirm(text: String) {
        _uiState.update {
            it.copy(confirmPassword = text)
        }

    }

    fun onChangeEmail(text: String) {
        _uiState.update {
            it.copy(email = text)
        }

    }

    fun onChangeFullName(text: String) {
        _uiState.update {
            it.copy(fullName = text)
        }
    }

    fun onChangeOTPInput(text: String) {
        _uiState.update {
            it.copy(otpInput = text)
        }
    }

    fun toggleOTPInputVisibility() {
        _uiState.update { it.copy(isOtpBottomSheetVisible = !_uiState.value.isOtpBottomSheetVisible) }
    }

    fun signUpUser() {
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
                when (signUpResponse) {
                    is NetworkResult.Success -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = signUpResponse.data.msg))
                        if (signUpResponse.data.success) {
                            analyticsHelper.logSignUpEvent(email = _uiState.value.email)

                            val response =
                                signUpRepository.triggerVerifyEmailOtp(OtpTriggerBody(email = _uiState.value.email))
                            when (response) {
                                is NetworkResult.Success -> {
                                    toggleOTPInputVisibility()

                                }

                                is NetworkResult.Error -> {

                                }

                                is NetworkResult.Exception -> {

                                }
                            }


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

    fun submitOTPInput(navigateToLoginScreen: () -> Unit) {

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val response = signUpRepository.validateVerifyEmailOtp(
                ValidateOtpBody(
                    email = _uiState.value.email,
                    otpPassword = _uiState.value.otpInput
                )
            )
            when(response){
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    if (response.data.success){
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "OTP Validated"))
                        navigateToLoginScreen()
                    }else{
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Invalid OTP"))
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "An unexpected error occurred"))
                }
                is NetworkResult.Exception -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "An unexpected exception occurred"))
                }
            }
        }
    }
}