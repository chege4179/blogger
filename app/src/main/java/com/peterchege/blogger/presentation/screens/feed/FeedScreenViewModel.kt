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
package com.peterchege.blogger.presentation.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.requests.LikePost
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.firebase.config.RemoteFeatureToggle
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.data.local.posts.likes.LikesLocalDataSource
import com.peterchege.blogger.domain.mappers.toEntity
import com.peterchege.blogger.domain.models.PostUI
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.NetworkInfoRepository
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed interface FeedScreenUiState {

    object Loading : FeedScreenUiState

    data class Success(val posts: List<PostUI>) : FeedScreenUiState

    data class Error(val message: String) : FeedScreenUiState

    object Empty : FeedScreenUiState
}


@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val likesLocalDataSource: LikesLocalDataSource,
    authRepository: AuthRepository,
    networkInfoRepository: NetworkInfoRepository,
    private val remoteFeatureToggle: RemoteFeatureToggle,


    ) : ViewModel() {
    val tag = FeedScreenViewModel::class.java.simpleName


    private val _isSyncing = MutableStateFlow(false)
    val isSyncing = _isSyncing.asStateFlow()

    val feedScreenUiState =postRepository.getAllPosts()
        .map<List<PostUI>, FeedScreenUiState> {
            Timber.tag("Here").i("Here")
            (FeedScreenUiState.Success(posts = it))
        }
        .onStart {
            emit(FeedScreenUiState.Loading)
        }
        .catch { emit(FeedScreenUiState.Error(message = it.message ?: it.localizedMessage)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = FeedScreenUiState.Empty
        )


    val networkStatus = networkInfoRepository.networkStatus
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NetworkStatus.Unknown
        )

    val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun refreshFeed(){
        _isSyncing.value = true
        viewModelScope.launch {
            postRepository.syncFeed()
            _isSyncing.value = false
        }
    }

    fun bookmarkPost(post: Post) {
        viewModelScope.launch {
            postRepository.insertSavedPost(post)
            _eventFlow.emit(UiEvent.ShowSnackbar(message = "Post added to bookmarks"))
        }
    }

    fun unBookmarkPost(post: Post) {
        viewModelScope.launch {
            postRepository.deleteSavedPostById(post.postId)
            _eventFlow.emit(UiEvent.ShowSnackbar(message = "Post removed from bookmarks"))
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