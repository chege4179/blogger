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
package com.peterchege.blogger.presentation.screens.dashboard.savedposts_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.room.entities.PostRecordWithCommentsLikesViews
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.domain.mappers.toExternalModel
import com.peterchege.blogger.domain.models.PostUI
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SavedPostScreenUiState {

    object Loading : SavedPostScreenUiState

    data class Success(val posts:List<Post>) : SavedPostScreenUiState

    data class Error(val message: String) : SavedPostScreenUiState

    object Empty : SavedPostScreenUiState
}

@HiltViewModel
class SavedPostScreenViewModel @Inject constructor(
    private val repository: PostRepository,
    private val authRepository: AuthRepository,

    ) : ViewModel() {


    val uiState = repository.getAllSavedPosts()
        .map <List<PostRecordWithCommentsLikesViews>,SavedPostScreenUiState>{
            val posts = it.map { it.toExternalModel() }
            SavedPostScreenUiState.Success(posts = posts)
        }
        .onStart {
            SavedPostScreenUiState.Empty
        }
        .catch {
            SavedPostScreenUiState.Error(message = "An unexpected error occurred")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = SavedPostScreenUiState.Loading
        )
}