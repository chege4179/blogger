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
import com.peterchege.blogger.core.api.requests.LikePost
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.data.local.posts.likes.LikesLocalDataSource
import com.peterchege.blogger.domain.mappers.toEntity
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SavedPostScreenUiState {

    object Loading : SavedPostScreenUiState

    data class Success(val posts: List<Post>) : SavedPostScreenUiState

    data class Error(val message: String) : SavedPostScreenUiState

    object Empty : SavedPostScreenUiState
}

@HiltViewModel
class SavedPostScreenViewModel @Inject constructor(
    private val repository: PostRepository,
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository,
    private val likesLocalDataSource: LikesLocalDataSource,

    ) : ViewModel() {

    val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )
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

    fun bookmarkPost(post: Post) {
        viewModelScope.launch {
            postRepository.insertSavedPost(post)

        }
    }

    fun unBookmarkPost(post: Post) {
        viewModelScope.launch {
            postRepository.deleteSavedPostById(post.postId)

        }
    }

    fun likePost(post: Post, user: User) {
        viewModelScope.launch {
            val likePost = LikePost(userId = user.userId, postId = post.postId)
            val response = postRepository.likePost(likePost)
            when (response) {
                is NetworkResult.Success -> {
                    response.data.like?.let {
                        likesLocalDataSource.insertLike(like = it.toEntity())
                    }
                }

                is NetworkResult.Error -> {

                }

                is NetworkResult.Exception -> {

                }
            }
        }
    }

    fun unLikePost(post: Post, user: User) {
        viewModelScope.launch {
            val likePost = LikePost(userId = user.userId, postId = post.postId)
            val response = postRepository.unlikePost(likePost)
            when (response) {
                is NetworkResult.Success -> {
                    likesLocalDataSource.deleteLike(postId = post.postId)
                }

                is NetworkResult.Error -> {

                }

                is NetworkResult.Exception -> {

                }
            }
        }
    }


}