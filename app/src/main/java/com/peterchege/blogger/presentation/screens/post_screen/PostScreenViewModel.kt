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
package com.peterchege.blogger.presentation.screens.post_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.requests.CommentBody
import com.peterchege.blogger.core.api.requests.FollowUser
import com.peterchege.blogger.core.api.requests.LikePost
import com.peterchege.blogger.core.api.requests.Viewer
import com.peterchege.blogger.core.api.responses.Comment
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.domain.mappers.toDomain
import com.peterchege.blogger.domain.models.PostUI
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.CommentRepository
import com.peterchege.blogger.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

sealed interface PostScreenUiState {
    object Loading : PostScreenUiState

    data class Success(val post: PostUI, val isUserLoggedIn:Boolean) : PostScreenUiState

    data class Error(val message: String) : PostScreenUiState

    object PostNotFound : PostScreenUiState
}

data class CommentUiState(
    val comments: List<Comment> = emptyList(),
    val newComment: String = "",
    val isCommentDialogOpen: Boolean = false
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
) : ViewModel() {
    val postId = savedStateHandle.get<String>("postId")
    private val postFlow = repository.getPostById(postId = postId ?: "")
    val authUserFlow = authRepository.getLoggedInUser()
    private val isUserLoggedIn = authRepository.isUserLoggedIn
    private val savedPostIdsFlow = repository.getSavedPostIds()

    private val _commentUiState = MutableStateFlow(CommentUiState())
    val commentUiState = _commentUiState.asStateFlow()

    private val _deleteUiState = MutableStateFlow(DeletePostUiState())
    val deletePostUiState = _deleteUiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val uiState: StateFlow<PostScreenUiState> =
        combine(postFlow, savedPostIdsFlow, authUserFlow,isUserLoggedIn) { post, savedPostIds, authUser,loggedIn ->
            if (post == null) {
                PostScreenUiState.PostNotFound
            } else {
                val postUi = post.toDomain(
                    isProfile = false,
                    isSaved = savedPostIds.contains(postId),
                    isLiked = if(loggedIn) post.likes.map { it.userId }.contains(authUser?._id) else false,
                )
                _commentUiState.value = _commentUiState.value.copy(comments = post.comments)
                PostScreenUiState.Success(post = postUi,isUserLoggedIn = loggedIn)
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
        _commentUiState.value = _commentUiState.value.copy(isCommentDialogOpen = false)

        savedStateHandle.get<String>("postId")?.let { postId ->
            val postedOn = SimpleDateFormat("dd/MM/yyyy").format(Date())
            val postedAt = SimpleDateFormat("hh:mm:ss").format(Date())
            postComment(
                CommentBody(
                    comment = _commentUiState.value.newComment,
                    username = user.username ?: "",
                    postedAt = postedAt,
                    postedOn = postedOn,
                    userId = user._id ?: "",
                    imageUrl = user?.imageUrl ?: "",
                    postId = postId,
                    postAuthor = ""
                ),
            )
        }
    }

    fun onDialogOpen() {
        _commentUiState.value = _commentUiState.value.copy(isCommentDialogOpen = true)

    }

    fun onDialogDismiss() {
        _commentUiState.value = _commentUiState.value.copy(isCommentDialogOpen = false)
    }

    fun deletePost() {


    }

    fun onDialogDeleteConfirm() {
        _deleteUiState.value = _deleteUiState.value.copy(isDeleteDialogOpen = false)
        viewModelScope.launch {
            val response = repository.deletePostFromApi(postId = postId ?: "")
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
            val username = user.username ?: ""
            val fullname = user.fullname ?: ""
            val userId = user._id ?: ""
            val response = repository.addView(
                Viewer(
                    viewerFullname = fullname,
                    viewerUsername = username,
                    viewerId = userId,
                    postId = postId ?: "",
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

    fun followUser(user:User,postAuthor: String) {
        val username = user.username
        val fullname = user.fullname
        val userId = user._id
        viewModelScope.launch {
            val followResponse = repository.followUser(
                FollowUser(
                    followerUsername = username!!,
                    followerFullname = fullname!!,
                    followerId = userId!!,
                    followedUsername =postAuthor,
                )
            )

        }
    }

    fun unfollowUser(user:User,postAuthor: String) {
        val username = user.username ?: ""
        val fullname = user.fullname ?: ""
        val userId = user?._id ?: ""
        viewModelScope.launch {
            val followResponse = repository.unfollowUser(
                FollowUser(
                    followerUsername = username,
                    followerFullname = fullname,
                    followerId = userId,
                    followedUsername = postAuthor,
                )
            )
        }
    }
    fun likePost(user:User,postAuthor: String) {

        val userId = user._id
        val username = user.username
        val fullname = user.fullname
        viewModelScope.launch {
            val likeResponse = repository.likePost(
                LikePost(
                    userId = userId,
                    username = username,
                    fullname = fullname,
                    postAuthor =postAuthor,
                    postId = postId ?:""

                )
            )
        }
    }
    fun unlikePost(user: User,postAuthor:String) {
        val userId = user._id
        val username = user.username
        val fullname = user.fullname
        viewModelScope.launch {
            val likeResponse = repository.unlikePost(
                LikePost(
                    userId = userId,
                    username = username,
                    fullname = fullname,
                    postAuthor = postAuthor,
                    postId = postId ?:""

                )
            )
        }
    }

    private fun addComment(comment: Comment) {
        val comments = ArrayList(_commentUiState.value.comments)
        comments.add(comment)
        _commentUiState.value = _commentUiState.value.copy(comments = comments)
    }

    private fun postComment(commentBody: CommentBody) {
        viewModelScope.launch {
            val commentResponse = commentRepository.postComment(commentBody)
            when(commentResponse){
                is NetworkResult.Success -> {
                    _commentUiState.value = _commentUiState.value.copy(newComment = "")
                    if (commentResponse.data.success) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = commentResponse.data.msg))
                        addComment(commentResponse.data.comment)

                    }
                }
                is NetworkResult.Exception -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = commentResponse.e.message ?: "Your Comment was not sent"))

                }
                is NetworkResult.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "Could not reach server... Please check your internet connection"))
                }
            }
        }
    }
}