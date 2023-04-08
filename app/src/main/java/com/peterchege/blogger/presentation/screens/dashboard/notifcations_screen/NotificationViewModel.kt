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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.requests.Notification
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {


    private var _notifications = mutableStateOf<List<Notification>>(emptyList())
    var notifications: State<List<Notification>> = _notifications

    private var _isLoading = mutableStateOf(false)
    var isLoading: State<Boolean> = _isLoading

    private var _user = MutableStateFlow<User?>(null)
    var user: StateFlow<User?> = _user

    init {
        loadUser()
        getNotifications()
    }

    private fun loadUser(){
        viewModelScope.launch {
            authRepository.getLoggedInUser().collectLatest {
                _user.value = it
            }
        }
    }


    private fun getNotifications() {
        getProfileUseCase(username = _user.value?.username ?: "").onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _isLoading.value = false
                    _notifications.value = result.data!!.user.notifications
                }
                is Resource.Loading -> {
                    _isLoading.value = true
                }
                is Resource.Error -> {
                    _isLoading.value = false
                }
            }

        }.launchIn(viewModelScope)

    }
}