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

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.peterchege.blogger.core.api.requests.LoginUser
import com.peterchege.blogger.core.util.*
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var _state = mutableStateOf(LoginScreenState())
    var state: State<LoginScreenState> = _state

    private var _isLoading = mutableStateOf(false)
    var isLoading: State<Boolean> = _isLoading

    private var _usernameState = mutableStateOf(TextFieldState())
    var usernameState: State<TextFieldState> = _usernameState

    private var _passwordState = mutableStateOf(TextFieldState())
    var passwordState: State<TextFieldState> = _passwordState

    private var _LoginResponseState = mutableStateOf(LoginResponse())
    var LoginResponseState: State<LoginResponse> = _LoginResponseState

    private val _passwordVisibility = mutableStateOf(false)
    var passwordVisibility: State<Boolean> = _passwordVisibility

    fun onChangePasswordVisibility() {
        _passwordVisibility.value = !_passwordVisibility.value
    }

    data class LoginScreenState(
        val isLoading: Boolean = false,
        val msg: String = "",
        val success: Boolean = false,
    )

    data class LoginResponse(
        val msg: String = "",
        val success: Boolean = false
    )

    fun OnChangeUsername(text: String) {
        _usernameState.value = TextFieldState(text = text)


    }

    fun OnChangePassword(text: String) {
        _passwordState.value = TextFieldState(text = text)

    }
    fun initiateLogin() {
        viewModelScope.launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                _isLoading.value = true
                if (_usernameState.value.text.length < 5 || _passwordState.value.text.length < 5) {
                    _isLoading.value = false
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "Credentials should be at least 5 characters long"))
                } else {
                    val loginUser = LoginUser(
                        _usernameState.value.text.trim(),
                        _passwordState.value.text.trim(),
                        token = token!!
                    )
                    try {
                        val response = repository.loginUser(loginUser)
                        _isLoading.value = false
                        if (!response.success) {
                            _eventFlow.emit(UiEvent.ShowSnackbar(message =response.msg))

                        }
                        if (response.success) {
                            response.user?.let {
                                repository.setLoggedInUser(user = it)
                            }
                            _eventFlow.emit(UiEvent.Navigate(route = Screens.DASHBOARD_SCREEN))
                        }
                    } catch (e: HttpException) {
                        _isLoading.value = false
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Server error...Please again later"))

                    } catch (e: IOException) {
                        _isLoading.value = false
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Could not connect to the server... Please try again"))
                    }
                }
            }catch (e:Exception){
                _eventFlow.emit(UiEvent.ShowSnackbar(message = "Unexpected error "))
            }
        }
    }
}