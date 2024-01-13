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
package com.peterchege.blogger.presentation.screens.author

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.peterchege.blogger.core.api.requests.FollowUser
import com.peterchege.blogger.core.api.requests.LikePost
import com.peterchege.blogger.core.api.requests.UnFollowUser
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.data.local.posts.likes.LikesLocalDataSource
import com.peterchege.blogger.data.local.posts.saved.SavedPostsDataSource
import com.peterchege.blogger.data.local.users.follower.FollowersLocalDataSource
import com.peterchege.blogger.data.local.users.following.FollowingLocalDataSource
import com.peterchege.blogger.domain.mappers.toEntity
import com.peterchege.blogger.domain.paging.ProfileScreenPostsPagingSource
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.PostRepository
import com.peterchege.blogger.domain.repository.ProfileRepository
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthorProfileScreenUiState {

    object Loading : AuthorProfileScreenUiState

    data class Success(
        val posts: Flow<PagingData<Post>>,
        val user: User,
        val isUserLoggedIn: Boolean,
        val isFollowingMe:Boolean,
        val isAuthUserFollowingBack:Boolean,
    ) : AuthorProfileScreenUiState

    data class Error(val message: String) : AuthorProfileScreenUiState


}


@HiltViewModel
class AuthorProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val followersLocalDataSource: FollowersLocalDataSource,
    private val followingLocalDataSource:FollowingLocalDataSource,
    private val likesLocalDataSource: LikesLocalDataSource,
    private val savedPostsDataSource: SavedPostsDataSource,
    private val postRepository: PostRepository,
) : ViewModel() {

    private val userId = savedStateHandle.getStateFlow(key = "userId", initialValue = "")

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val isUserLoggedIn = authRepository.isUserLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false,
        )

    val authUser = authRepository.getLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null,
        )

    private val followingUserIds = followingLocalDataSource.getAllAuthUserFollowingsIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    private val followerUserIds = followersLocalDataSource.getAllAuthUserFollowerIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val likedPostIds = likesLocalDataSource.getAllLikes()
        .map { it.map { it.likepostId } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

    val savedPostIds = savedPostsDataSource.getSavedPostIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )


    val uiState = combine(
        profileUseCase(userId = userId.value),
        isUserLoggedIn,
        followerUserIds,
        followingUserIds,
    ) { result, loggedIn ,followerIds,followingIds  ->
        when (result) {
            is Resource.Success -> {
                val isFollowingMe = followerIds.contains(result.data?.user?.userId ?:"")
                val isAuthUserFollowingBack = followingIds.contains(result.data?.user?.userId ?:"")

                AuthorProfileScreenUiState.Success(
                    posts = getPaginatedPostsByUserId(userId.value),
                    user = result.data!!.user,
                    isUserLoggedIn = loggedIn,
                    isFollowingMe = isFollowingMe,
                    isAuthUserFollowingBack = isAuthUserFollowingBack
                )
            }

            is Resource.Error -> {
                AuthorProfileScreenUiState.Error(
                    message = result.message ?: "An unexpected error occurred"
                )
            }

            is Resource.Loading -> {
                AuthorProfileScreenUiState.Loading
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AuthorProfileScreenUiState.Loading
    )


    @OptIn(ExperimentalPagingApi::class)
    fun getPaginatedPostsByUserId(userId: String): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ProfileScreenPostsPagingSource(
                    profileRepository = profileRepository,
                    userId = userId
                )
            }
        ).flow
    }
    fun followUser(userIdToBeFollowed: String, userId: String) {
        viewModelScope.launch {
            val followResponse = profileRepository.followUser(
                FollowUser(
                    followerUserId = userId,
                    followedUserId = userIdToBeFollowed,
                )
            )
            when (followResponse) {
                is NetworkResult.Success -> {
                    if (followResponse.data.success) {
                        followResponse.data.newFollower?.let {
                            authRepository.addUserFollowing(following = it)
                        }
                    }
                }

                is NetworkResult.Error -> {

                }

                is NetworkResult.Exception -> {

                }
            }

        }
    }

    fun unfollowUser(userId:String,unfollowedUserId:String) {
        viewModelScope.launch {
            val followResponse = profileRepository.unfollowUser(
                UnFollowUser(
                    userId = userId,
                    unfollowedUserId = unfollowedUserId
                )
            )
            when (followResponse) {
                is NetworkResult.Success -> {
                    if (followResponse.data.success) {
                        authRepository.removeUserFollowing(userId = unfollowedUserId)
                    }
                }
                is NetworkResult.Error -> {

                }

                is NetworkResult.Exception -> {

                }
            }
        }
    }

    fun bookmarkPost(post: Post){
        viewModelScope.launch {
            postRepository.insertSavedPost(post)
            _eventFlow.emit(UiEvent.ShowSnackbar(message = "Post added to bookmarks"))
        }
    }
    fun unBookmarkPost(post: Post){
        viewModelScope.launch {
            postRepository.deleteSavedPostById(post.postId)
            _eventFlow.emit(UiEvent.ShowSnackbar(message = "Post removed from bookmarks"))
        }
    }
    fun likePost(post: Post,user:User){
        viewModelScope.launch {
            val likePost = LikePost(userId = user.userId,postId = post.postId)
            val response = postRepository.likePost(likePost)
            when(response){
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
    fun unLikePost(post: Post,user:User){
        viewModelScope.launch {
            val likePost = LikePost(userId = user.userId,postId = post.postId)
            val response = postRepository.unlikePost(likePost)
            when(response){
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