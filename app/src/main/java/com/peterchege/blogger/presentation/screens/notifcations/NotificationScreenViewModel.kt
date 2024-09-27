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
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.peterchege.blogger.core.api.responses.models.Notification
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.paging.NotificationsPagingSource
import com.peterchege.blogger.domain.paging.ProfileScreenPostsPagingSource
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.NotificationRepository

import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

sealed interface NotificationScreenUiState {

    object UserNotLoggedIn: NotificationScreenUiState

    object Loading : NotificationScreenUiState

    data class Success(val notifications:Flow<PagingData<Notification>>) : NotificationScreenUiState

    data class Error(val message: String) : NotificationScreenUiState


}



@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _activeNotificationFilter = MutableStateFlow("All")
    val activeNotificationFilter = _activeNotificationFilter.asStateFlow()


    val isUserLoggedIn  = authRepository.isUserLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )
    val authUser = authRepository.getLoggedInUser()
        .filterNot {
            it?.userId == ""
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    val uiState = combine(flow = isUserLoggedIn,flow2 = authUser){ isLoggedIn,user ->
        if (isLoggedIn){
            val notificationsPagingData = getNotificationByUserId()
            NotificationScreenUiState.Success(notifications = notificationsPagingData)
        }else{
            NotificationScreenUiState.UserNotLoggedIn
        }
    }
        .onStart { NotificationScreenUiState.Loading }
        .catch { NotificationScreenUiState.Error(message = "An unexpected error occurred") }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = NotificationScreenUiState.Loading
    )

    @OptIn(ExperimentalPagingApi::class)
    fun getNotificationByUserId(): Flow<PagingData<Notification>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NotificationsPagingSource(
                    notificationRepository = notificationRepository,
                )
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun setNotificationFilter(notificationFilter:String){
        _activeNotificationFilter.update { notificationFilter }
    }
}