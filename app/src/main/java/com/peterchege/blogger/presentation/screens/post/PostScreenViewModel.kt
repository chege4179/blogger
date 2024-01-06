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
package com.peterchege.blogger.presentation.screens.post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.peterchege.blogger.core.api.requests.CommentBody
import com.peterchege.blogger.core.api.requests.FollowUser
import com.peterchege.blogger.core.api.requests.LikePost
import com.peterchege.blogger.core.api.requests.Viewer
import com.peterchege.blogger.core.api.responses.models.Comment
import com.peterchege.blogger.core.api.responses.models.CommentWithUser
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.data.local.posts.likes.LikesLocalDataSource
import com.peterchege.blogger.domain.mappers.toDomain
import com.peterchege.blogger.domain.mappers.toEntity
import com.peterchege.blogger.domain.models.PostUI
import com.peterchege.blogger.domain.paging.CommentsPagingSource
import com.peterchege.blogger.domain.paging.ProfileScreenPostsPagingSource
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.CommentRepository
import com.peterchege.blogger.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.collections.ArrayList

sealed interface PostScreenUiState {
    object Loading : PostScreenUiState

    data class Success(
        val post: PostUI,
        val isUserLoggedIn: Boolean,
        val comments: Flow<PagingData<CommentWithUser>>,
    ) : PostScreenUiState

    data class Error(val message: String) : PostScreenUiState

    object PostNotFound : PostScreenUiState
}

data class CommentUiState(
    val newComment: String = "",
    val isCommentsBottomSheetOpen: Boolean = false
)

data class DeletePostUiState(
    val isDeleteDialogOpen: Boolean = false,
)

@HiltViewModel
class PostScreenViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    private val savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val repository: PostRepository,
    private val likesLocalDataSource: LikesLocalDataSource,

    ) : ViewModel() {

    val postId = savedStateHandle.getStateFlow(key = "postId", initialValue = "")

    private val postFlow = repository.getPostById(postId = postId.value)

    val authUserFlow = authRepository.getLoggedInUser()

    private val isUserLoggedIn = authRepository.isUserLoggedIn

    private val likedPostIdsFlow =
        likesLocalDataSource.getAllLikes().map { it.map { it.likepostId } }

    private val savedPostIdsFlow = repository.getSavedPostIds()

    private val _commentUiState = MutableStateFlow(CommentUiState())
    val commentUiState = _commentUiState.asStateFlow()

    private val _deleteUiState = MutableStateFlow(DeletePostUiState())
    val deletePostUiState = _deleteUiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val uiState: StateFlow<PostScreenUiState> =
        combine(
            postFlow,
            savedPostIdsFlow,
            authUserFlow,
            isUserLoggedIn,
            likedPostIdsFlow
        ) { post, savedPostIds, authUser, loggedIn, likedPostIds ->
            if (post == null) {
                PostScreenUiState.PostNotFound
            } else {
                val postUi = post.toDomain(
                    isProfile = false,
                    isSaved = savedPostIds.contains(postId.value),
                    isLiked = likedPostIds.contains(postId.value),
                )
                PostScreenUiState.Success(
                    post = postUi,
                    isUserLoggedIn = loggedIn,
                    comments = getPaginatedPostsByPostId(postId = postId.value)
                )
            }
        }.onStart {
            emit(PostScreenUiState.Loading)
        }
            .catch {
                emit(PostScreenUiState.Error(message = it.message ?: it.localizedMessage))
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = PostScreenUiState.Loading
            )


    fun onDialogConfirm(user: User) {
        _commentUiState.value = _commentUiState.value.copy(isCommentsBottomSheetOpen = false)

        savedStateHandle.get<String>("postId")?.let { postId ->
            postComment(
                CommentBody(
                    commentBody = _commentUiState.value.newComment,
                    userId = user.userId,
                    commentPostId = postId
                ),
            )
        }
    }

    fun onDialogOpen() {
        _commentUiState.value = _commentUiState.value.copy(isCommentsBottomSheetOpen = true)

    }

    fun onDialogDismiss() {
        _commentUiState.value = _commentUiState.value.copy(isCommentsBottomSheetOpen = false)
    }

    fun deletePost() {


    }

    @OptIn(ExperimentalPagingApi::class)
    fun getPaginatedPostsByPostId(postId: String): Flow<PagingData<CommentWithUser>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CommentsPagingSource(
                    commentRepository = commentRepository,
                    postId = postId
                )
            }
        ).flow
    }

    fun onDialogDeleteConfirm() {
        _deleteUiState.value = _deleteUiState.value.copy(isDeleteDialogOpen = false)
        viewModelScope.launch {
            val response = repository.deletePostFromApi(postId = postId.value)
            when (response) {
                is NetworkResult.Success -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = response.data.msg))
                }

                is NetworkResult.Exception -> {
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            message =
                            response.e.message ?: response.e.localizedMessage
                        )
                    )
                }

                is NetworkResult.Error -> {
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            message = response.message ?: "An unexpected error occurred"
                        )
                    )
                }

            }

        }
    }

    private fun addViewCount(user: User) {
        viewModelScope.launch {
            delay(3000L)
            val response = repository.addView(
                Viewer(
                    postId = postId.value ?: "",
                    userId = user.userId,
                )
            )
        }

    }

    fun onDialogDeleteOpen() {
        _deleteUiState.value = _deleteUiState.value.copy(isDeleteDialogOpen = true)

    }

    fun onDialogDeleteDismiss() {
        _deleteUiState.value = _deleteUiState.value.copy(isDeleteDialogOpen = false)
    }

    fun onChangeComment(text: String) {
        _commentUiState.value = _commentUiState.value.copy(newComment = text)
    }

    fun savePostToRoom(post: Post) {
        viewModelScope.launch {
            try {
                repository.insertSavedPost(post)
                _eventFlow.emit(UiEvent.ShowSnackbar(message = "Your post has been saved "))

            } catch (e: IOException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        message =
                        "An unexpected error has occurred while trying to save yout post"
                    )
                )
            }
        }
    }

    fun deletePostFromRoom(postId: String) {
        viewModelScope.launch {
            try {
                repository.deleteSavedPostById(postId)
                _eventFlow.emit(UiEvent.ShowSnackbar(message = "Your post has been deleted from the saved posts "))
            } catch (e: IOException) {
                _eventFlow.emit(UiEvent.ShowSnackbar(message = "An error occurred deleting your saved Post"))
            }
        }
    }

    fun followUser(user: User, postAuthorId: String) {
        viewModelScope.launch {
            val followResponse = repository.followUser(
                FollowUser(
                    followerUserId = user.userId,
                    followedUserId = postAuthorId
                )
            )

        }
    }

    fun unfollowUser(user: User, postAuthorId: String) {
        viewModelScope.launch {
            val followResponse = repository.unfollowUser(
                FollowUser(
                    followerUserId = user.userId,
                    followedUserId = postAuthorId
                )
            )
        }
    }

    fun likePost(post: Post, user: User) {
        viewModelScope.launch {
            val likePost = LikePost(userId = user.userId, postId = post.postId)
            val response = repository.likePost(likePost)
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
            val response = repository.unlikePost(likePost)
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

    private fun addComment(comment: Comment) {

    }

    private fun postComment(commentBody: CommentBody) {
        viewModelScope.launch {
            val commentResponse = commentRepository.postComment(commentBody)
            when (commentResponse) {
                is NetworkResult.Success -> {
                    _commentUiState.value = _commentUiState.value.copy(newComment = "")
                    if (commentResponse.data.success) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = commentResponse.data.msg))
                        addComment(commentResponse.data.comment)

                    }
                }

                is NetworkResult.Exception -> {
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            message = commentResponse.e.message ?: "Your Comment was not sent"
                        )
                    )

                }

                is NetworkResult.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "Could not reach server... Please check your internet connection"))
                }
            }
        }
    }
}