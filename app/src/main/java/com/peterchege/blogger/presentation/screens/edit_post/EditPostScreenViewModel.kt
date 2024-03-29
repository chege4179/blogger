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
package com.peterchege.blogger.presentation.screens.edit_post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.requests.UpdatePost
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.PostAuthor
import com.peterchege.blogger.core.api.responses.models.PostCount
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditPostFormState(
    val post: Post? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class EditPostScreenViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _formState = MutableStateFlow<EditPostFormState>(EditPostFormState())
    val formState = _formState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun setEditPost(post: Post) {
        _formState.update { it.copy(post = post) }
    }

    fun onChangePostBody(text: String) {
        _formState.update {
            it.copy(post = _formState.value.post?.copy(postBody = text))
        }
    }

    fun onChangePostTitle(text: String) {
        _formState.update {
            it.copy(post = _formState.value.post?.copy(postTitle = text))
        }
    }

    fun updatePost() {
        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true) }
            val updatePost = UpdatePost(
                postId = _formState.value.post?.postId ?: return@launch,
                postTitle = _formState.value.post?.postTitle ?: return@launch,
                postBody = _formState.value.post?.postBody ?: return@launch
            )
            val response = postRepository.updatePost(updatePost)
            when (response) {
                is NetworkResult.Success -> {
                    _formState.update { it.copy(isLoading = false) }
                    if (response.data.success) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Post updated successfully"))
                    } else {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Failed to update post"))
                    }
                }

                is NetworkResult.Error -> {
                    _formState.update { it.copy(isLoading = false) }
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "An error occurred"))
                }

                is NetworkResult.Exception -> {
                    _formState.update { it.copy(isLoading = false) }
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "An exception occurred"))
                }
            }
        }
    }
}
