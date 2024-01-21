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

@HiltViewModel
class EditPostScreenViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel(){

    private val _editPost = MutableStateFlow<Post?>(null)
    val editPost = _editPost.asStateFlow()

    private val _eventFlow= MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun setEditPost(post: Post){
        _editPost.update { post }
    }

    fun onChangePostBody(text:String){
        _editPost.update {
            it?.copy(postBody = text)
        }
    }
    fun onChangePostTitle(text:String){
        _editPost.update {
            it?.copy(postTitle = text)
        }
    }

    fun updatePost(){
        viewModelScope.launch {
            val updatePost = UpdatePost(
                postId = _editPost.value?.postId ?:return@launch,
                postTitle = _editPost.value?.postTitle ?: return@launch,
                postBody = _editPost.value?.postBody ?:return@launch
            )
            val response = postRepository.updatePost(updatePost)
            when(response){
                is NetworkResult.Success -> {
                    if (response.data.success){
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Post updated successfully"))
                    }else{
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Failed to update post"))
                    }

                }
                is NetworkResult.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "An error occurred"))
                }
                is NetworkResult.Exception -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "An exception occurred"))
                }
            }
        }
    }
}
