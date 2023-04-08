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

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.api.requests.CommentBody
import com.peterchege.blogger.core.api.requests.FollowUser
import com.peterchege.blogger.core.api.requests.LikePost
import com.peterchege.blogger.core.api.requests.Viewer
import com.peterchege.blogger.core.api.responses.Comment
import com.peterchege.blogger.core.api.responses.Like
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.toPostRecord
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.mappers.toExternalModel
import com.peterchege.blogger.domain.repository.PostRepository
import com.peterchege.blogger.domain.use_case.GetPostUseCase
import com.peterchege.blogger.domain.use_case.PostCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase,
    private val postCommentUseCase: PostCommentUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val sharedPreferences: SharedPreferences,
    private val repository: PostRepository
) : ViewModel() {

    private val _state = mutableStateOf(PostDetailState())
    val state: State<PostDetailState> = _state

    private var _commentInputState = mutableStateOf("")
    var commentInputState: State<String> = _commentInputState

    private var _openDialogState = mutableStateOf(false)
    var openDialogState: State<Boolean> = _openDialogState

    private var _openDeleteDialogState = mutableStateOf(false)
    var openDeleteDialogState: State<Boolean> = _openDeleteDialogState

    private var _commentResponseState = mutableStateOf(CommentResponseState())
    var commentResponseState: State<CommentResponseState> = _commentResponseState

    private val _comments = mutableStateOf<List<Comment>>(emptyList())
    var comments: State<List<Comment>> = _comments


    private var _isLiked = mutableStateOf(false)
    var isLiked: State<Boolean> = _isLiked

    private var _isSaved = mutableStateOf(false)
    var isSaved: State<Boolean> = _isSaved

    private var _isFollowing = mutableStateOf(false)
    var isFollowing: State<Boolean> = _isFollowing

    private var _isMyPost = mutableStateOf(false)
    var isMyPost: State<Boolean> = _isMyPost

    private var _source = mutableStateOf("")
    var source: State<String> = _source

    data class PostDetailState(
        val isLoading: Boolean = false,
        val post: Post? = null,
        val error: String = "",
    )

    data class CommentResponseState(
        val msg: String = "",
        val isLoading: Boolean = false,
        val success: Boolean = false,
    )

    init {
        savedStateHandle.get<String>("postId")?.let { postId ->
            savedStateHandle.get<String>("source")?.let { source ->
                _source.value = source
                Log.e("source", source)

                if (source == Constants.API_SOURCE) {
                    getPost(postId = postId)
                    getSavedPostState(postId = postId)
                    addViewCount()

                } else if (source == Constants.ROOM_SOURCE) {
                    getPostFromRoom(postId = postId)
                    checkIsMyPost()

                }
            }
        }
    }

    private fun getPost(postId: String) {
        getPostUseCase(postId = postId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = PostDetailState(post = result.data)
                    _comments.value = result.data!!.comments
                    checkIsMyPost()
                    _isLiked.value = getIsLikedState(result.data!!.likes)

                }
                is Resource.Error -> {
                    _state.value = PostDetailState(
                        error = result.message ?: "This does not exist or has been deleted"
                    )
                }
                is Resource.Loading -> {
                    _state.value = PostDetailState(isLoading = true)

                }
            }

        }.launchIn(viewModelScope)
    }

    fun onDialogConfirm(scaffoldState: ScaffoldState) {
        _openDialogState.value = false
        savedStateHandle.get<String>("postId")?.let { postId ->
            Log.d("Comment value", _commentInputState.value)
            Log.d("Comment id", postId)
            Log.d(
                "Comment Logged In",
                sharedPreferences.getString(Constants.LOGIN_USERNAME, null)!!
            )
            Log.d("Comment postedBy", _state.value.post!!.postAuthor)
            val postedOn = SimpleDateFormat("dd/MM/yyyy").format(Date())
            val postedAt = SimpleDateFormat("hh:mm:ss").format(Date())
            postComment(
                CommentBody(
                    comment = _commentInputState.value,
                    username = sharedPreferences.getString(Constants.LOGIN_USERNAME, null)!!,
                    postedAt = postedAt,
                    postedOn = postedOn,
                    userId = sharedPreferences.getString(Constants.LOGIN_ID, null)!!,
                    imageUrl = sharedPreferences.getString(Constants.LOGIN_IMAGEURL, null)!!,
                    postId = _state.value.post!!._id,
                    postAuthor = _state.value.post!!.postAuthor

                ),
                scaffoldState = scaffoldState
            )
        }
    }

    fun onDialogOpen() {
        _openDialogState.value = true
    }

    fun onDialogDismiss() {
        _openDialogState.value = false
    }

    fun deletePost(scaffoldState: ScaffoldState) {


    }

    fun onDialogDeleteConfirm(scaffoldState: ScaffoldState) {
        _openDeleteDialogState.value = false
        viewModelScope.launch {
            try {
                val response = repository.deletePostFromApi(_state.value.post!!._id)
                scaffoldState.snackbarHostState.showSnackbar(
                    message = response.msg
                )

            } catch (e: HttpException) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Server error...Please try again later"
                )
            } catch (e: IOException) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Could not reach the internet....Please check your internet connection"
                )
            }
        }
    }

    private fun addViewCount() {
        viewModelScope.launch {
            delay(3000L)
            val username = sharedPreferences.getString(Constants.LOGIN_USERNAME, null)
            val fullname = sharedPreferences.getString(Constants.LOGIN_FULLNAME, null)
            val userId = sharedPreferences.getString(Constants.LOGIN_ID, null)
            try {
                val response = repository.addView(
                    Viewer(
                        viewerFullname = fullname!!,
                        viewerUsername = username!!,
                        viewerId = userId!!,
                        postId = _state.value.post!!._id,
                    )
                )
                Log.e("view response", response.msg)

            } catch (e: HttpException) {
                Log.e("view http error", e.localizedMessage ?: "Server error")
            } catch (e: IOException) {
                Log.e("view io error", e.localizedMessage ?: "internet issue")
            }
        }

    }

    fun onDialogDeleteOpen() {
        _openDeleteDialogState.value = true
    }

    fun onDialogDeleteDismiss() {
        _openDeleteDialogState.value = false
    }

    fun OnChangeComment(text: String) {
        _commentInputState.value = text
    }

    fun savePostToRoom(scaffoldState: ScaffoldState) {
        viewModelScope.launch {
            try {
                Log.e("insert", "inserted")
                _state.value.post?.let {
                    repository.insertPost(it)
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Your post has been saved "
                    )
                }

                _isSaved.value = true
            } catch (e: IOException) {

            }
        }
    }

    fun deletePostFromRoom(scaffoldState: ScaffoldState) {
        viewModelScope.launch {
            try {
                Log.e("deleted", "deleted")
                _state.value.post?.let {
                    repository.deletePostById(_state.value.post!!._id)
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Your post has been deleted from the saved posts "
                    )
                }
                _isSaved.value = false
            } catch (e: IOException) {

            }
        }
    }

    private fun getSavedPostState(postId: String) {
        viewModelScope.launch {
            try {
                val post = repository.getPostFromRoom(postId = postId)
                _isSaved.value = post != null
            } catch (e: IOException) {

            }

        }


    }

    fun getIsLikedState(likes: List<Like>): Boolean {
        val userId = sharedPreferences.getString(Constants.LOGIN_ID, null)
        val IdLikes = likes.map { it.userId }
        return IdLikes.contains(userId)
    }

    private fun checkIsMyPost() {
        val username = sharedPreferences.getString(Constants.LOGIN_USERNAME, null)

        _state.value.post?.let {
            _isMyPost.value = it.postAuthor == username
            Log.e("postAUTHOR", _state.value.post!!.postAuthor)
        }

    }

    fun followUser() {
        try {
            val username = sharedPreferences.getString(Constants.LOGIN_USERNAME, null)
            val fullname = sharedPreferences.getString(Constants.LOGIN_FULLNAME, null)
            val userId = sharedPreferences.getString(Constants.LOGIN_ID, null)
            viewModelScope.launch {
                val followResponse = repository.followUser(
                    FollowUser(
                        followerUsername = username!!,
                        followerFullname = fullname!!,
                        followerId = userId!!,
                        followedUsername = _state.value.post!!.postAuthor,
                    )
                )
                if (followResponse.success) {
                    _isFollowing.value = true
                }
            }

        } catch (e: HttpException) {
            Log.e("http error", e.localizedMessage ?: "A http error occurred")
        } catch (e: IOException) {
            Log.e("io error", e.localizedMessage ?: "An io error occurred")
        }


    }

    fun unfollowUser() {
        try {
            val username = sharedPreferences.getString(Constants.LOGIN_USERNAME, null)
            val fullname = sharedPreferences.getString(Constants.LOGIN_FULLNAME, null)
            val userId = sharedPreferences.getString(Constants.LOGIN_ID, null)
            viewModelScope.launch {
                val followResponse = repository.unfollowUser(
                    FollowUser(
                        followerUsername = username!!,
                        followerFullname = fullname!!,
                        followerId = userId!!,
                        followedUsername = _state.value.post!!.postAuthor,
                    )
                )
                if (followResponse.success) {
                    _isFollowing.value = false
                }
            }

        } catch (e: HttpException) {
            Log.e("http error", e.localizedMessage ?: "A http error occurred")
        } catch (e: IOException) {
            Log.e("io error", e.localizedMessage ?: "An io error occurred")
        }

    }

    private fun getPostFromRoom(postId: String) {
        try {
            viewModelScope.launch {
                _state.value = PostDetailState(
                    true,
                )
                val post = repository.getPostFromRoom(postId = postId)
                if (post != null) {
                    _state.value = PostDetailState(
                        isLoading = false,
                        post = post.toExternalModel()
                    )
                    checkIsMyPost()
                }
            }
        } catch (e: IOException) {
            Log.e("io error", e.localizedMessage ?: "An error occurred")
        }
    }

    fun likePost(scaffoldState: ScaffoldState) {
        _isLiked.value = true
        val userId = sharedPreferences.getString(Constants.LOGIN_ID, null)
        val username = sharedPreferences.getString(Constants.LOGIN_USERNAME, null)
        val fullname = sharedPreferences.getString(Constants.LOGIN_FULLNAME, null)
        viewModelScope.launch {
            try {
                val likeResponse = repository.likePost(
                    LikePost(
                        userId = userId!!,
                        username = username!!,
                        fullname = fullname!!,
                        postAuthor = _state.value.post!!.postAuthor,
                        postId = _state.value.post!!._id

                    )
                )
                if (likeResponse.success) {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = likeResponse.msg
                    )

                }

            } catch (e: HttpException) {
                Log.e("http error", e.localizedMessage ?: "An error occurred")
            } catch (e: IOException) {
                Log.e("io error", e.localizedMessage ?: "An error occurred")
            }

        }
    }

    fun unlikePost(scaffoldState: ScaffoldState) {
        _isLiked.value = false
        val userId = sharedPreferences.getString(Constants.LOGIN_ID, null)
        val username = sharedPreferences.getString(Constants.LOGIN_USERNAME, null)
        val fullname = sharedPreferences.getString(Constants.LOGIN_FULLNAME, null)
        viewModelScope.launch {
            try {
                val likeResponse = repository.unlikePost(
                    LikePost(
                        userId = userId!!,
                        username = username!!,
                        fullname = fullname!!,
                        postAuthor = _state.value.post!!.postAuthor,
                        postId = _state.value.post!!._id

                    )
                )
                if (likeResponse.success) {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = likeResponse.msg
                    )

                }

            } catch (e: HttpException) {
                Log.e("http error", e.localizedMessage ?: "An error occurred")
            } catch (e: IOException) {
                Log.e("io error", e.localizedMessage ?: "An error occurred")
            }

        }
    }

    private fun postComment(commentBody: CommentBody, scaffoldState: ScaffoldState) {
        postCommentUseCase(commentBody).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _commentResponseState.value =
                        CommentResponseState(result.data!!.msg, false, result.data.success)
                    _commentInputState.value = ""
                    if (result.data.success) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = result.data!!.msg
                        )
                        val newComment = result.data.comment
                        var oldcomments = _state.value.post?.comments
                        oldcomments = oldcomments?.plus(newComment)
                        _comments.value = oldcomments ?: emptyList()

                    }

                }
                is Resource.Error -> {
                    _commentResponseState.value =
                        CommentResponseState(result.data!!.msg, false, result.data.success)
                }
                is Resource.Loading -> {
                    _commentResponseState.value =
                        CommentResponseState("posting your comment......", true, false)

                }
            }
        }.launchIn(viewModelScope)
    }
}