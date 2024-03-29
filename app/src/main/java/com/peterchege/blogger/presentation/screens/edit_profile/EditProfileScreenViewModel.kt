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
package com.peterchege.blogger.presentation.screens.edit_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileFormState(
    val imageUrl: Uri? = null,
    val userId:String ="",
    val fullName:String = "",
    val username:String = "",

    val isLoading:Boolean = false
)
@HiltViewModel
class EditProfileScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,

):ViewModel() {

    val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    private val _formState = MutableStateFlow(EditProfileFormState())
    val formState = _formState.asStateFlow()

    private val _eventFlow= MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onChangeUsername(text:String){
        _formState.update { it.copy(username = text) }
    }

    fun onChangeFullName(text: String){
        _formState.update { it.copy(fullName = text) }
    }

    fun onChangeImageUri(text: Uri){
        _formState.update { it.copy(imageUrl = text) }
    }

    fun onChangeUserId(text: String){
        _formState.update { it.copy(userId = text) }
    }

    fun onSaveProfile(){
        viewModelScope.launch {
            if (_formState.value.username.isBlank()) {
                _eventFlow.emit(UiEvent.ShowSnackbar("Please fill in the username"))
                return@launch
            }
            if (_formState.value.fullName.isBlank()) {
                _eventFlow.emit(UiEvent.ShowSnackbar("Please fill in the password"))
                return@launch
            }
            _formState.update { it.copy(isLoading = true) }
            val response = profileRepository.updateUserInfo(updateUser = _formState.value)
            when(response){
                is NetworkResult.Success -> {
                    _formState.update { it.copy(isLoading = false) }
                    _eventFlow.emit(UiEvent.ShowSnackbar(response.data.msg))
                    response.data.user?.let {
                        authRepository.setLoggedInUser(it)
                    }
                }
                is NetworkResult.Error -> {
                    _formState.update { it.copy(isLoading = false) }
                    _eventFlow.emit(UiEvent.ShowSnackbar("An unexpected error occurred"))
                }
                is NetworkResult.Exception -> {
                    _formState.update { it.copy(isLoading = false) }
                    _eventFlow.emit(UiEvent.ShowSnackbar("An unexpected exception occurred"))
                }
            }
        }

    }







}