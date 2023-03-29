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

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.requests.Notification
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    val username = sharedPreferences.getString(Constants.LOGIN_USERNAME, null)
    private var _notifications = mutableStateOf<List<Notification>>(emptyList())
    var notifications: State<List<Notification>> = _notifications

    private var _isLoading = mutableStateOf(false)
    var isLoading: State<Boolean> = _isLoading

    init {
        getNotifications()
    }


    private fun getNotifications() {
        getProfileUseCase(username!!).onEach { result ->
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