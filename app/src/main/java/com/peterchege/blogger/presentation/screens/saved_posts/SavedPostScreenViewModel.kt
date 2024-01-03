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
package com.peterchege.blogger.presentation.screens.saved_posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.room.entities.SavePost
import com.peterchege.blogger.domain.mappers.toExternalModel
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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
        .map {
            SavedPostScreenUiState.Success(posts = it)
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