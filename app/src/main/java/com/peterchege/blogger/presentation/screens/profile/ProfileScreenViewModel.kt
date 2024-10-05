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
package com.peterchege.blogger.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.peterchege.blogger.core.firebase.analytics.AnalyticsHelper
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.data.local.posts.likes.LikesLocalDataSource
import com.peterchege.blogger.data.local.users.follower.FollowersLocalDataSource
import com.peterchege.blogger.data.local.users.following.FollowingLocalDataSource
import com.peterchege.blogger.domain.mappers.toEntity
import com.peterchege.blogger.domain.paging.ProfileScreenPostsPagingSource
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.NetworkInfoRepository
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.domain.repository.PostRepository
import com.peterchege.blogger.domain.repository.ProfileRepository
import com.peterchege.blogger.domain.use_case.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface ProfileScreenUiState {

    object UserNotLoggedIn : ProfileScreenUiState

    object Loading : ProfileScreenUiState

    data class Success(
        val posts: Flow<PagingData<Post>>,
        val user: User
    ) : ProfileScreenUiState

    data class Error(val message: String) : ProfileScreenUiState

    object Empty : ProfileScreenUiState
}
data class DeletePostUiState(
    val isDeletePostDialogOpen:Boolean = false,
    val post: Post? = null
)

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val profileUseCase: GetProfileUseCase,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val analyticsHelper: AnalyticsHelper,
    private val networkInfoRepository: NetworkInfoRepository,
    private val followersLocalDataSource: FollowersLocalDataSource,
    private val followingLocalDataSource: FollowingLocalDataSource,
    private val likesLocalDataSource: LikesLocalDataSource,
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _deletePostUiState = MutableStateFlow(DeletePostUiState())
    val deletePostUiState = _deletePostUiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val networkStatus = networkInfoRepository.networkStatus
        .stateIn(
            scope = viewModelScope,
            initialValue = NetworkStatus.Unknown,
            started = SharingStarted.WhileSubscribed(5000L)
        )

    private val isUserLoggedIn = authRepository.isUserLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    val authUser = authRepository.getLoggedInUser()
        .filterNot {
            it?.userId == ""
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    val uiState = combine(authUser, isUserLoggedIn) { user, loggedIn ->
        if (loggedIn) {
            val response = profileRepository.getMyProfile()
            when (response) {
                is NetworkResult.Success -> {
                    if (response.data?.user != null) {
                        ProfileScreenUiState.Success(
                            posts = getPaginatedPostsByUserId(userId = response.data.user.userId),
                            user = response.data.user,
                        )
                    } else {
                        ProfileScreenUiState.Error(message = "Failed to load users information")
                    }

                }
                else -> {
                    ProfileScreenUiState.Error(message = "Error")
                }
            }
        } else {
            ProfileScreenUiState.UserNotLoggedIn
        }

    }
        .onStart { ProfileScreenUiState.Loading }
        .catch { ProfileScreenUiState.Error(message = "An error occurred") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ProfileScreenUiState.Loading
        )

    fun toggleDeletePostDialogState(){
        val initialState = _deletePostUiState.value.isDeletePostDialogOpen
        _deletePostUiState.update {
            it.copy(isDeletePostDialogOpen = !initialState)
        }
    }

    fun setPostToBeDeleted(post: Post?){
        _deletePostUiState.update {
            it.copy(post = post)
        }
    }

    fun saveUserLikes(userId: String) {
        viewModelScope.launch {
            val response = profileRepository.getUserLikes(userId)
            when (response) {
                is NetworkResult.Success -> {
                    response.data.likes?.let { likes ->
                        likesLocalDataSource.deleteAllLikes()
                        likesLocalDataSource.insertLikes(likes = likes.map { it.toEntity() })
                    }
                }
                is NetworkResult.Error -> {

                }

                is NetworkResult.Exception -> {

                }
            }
        }
    }

    fun saveUserFollowing(userId: String) {
        viewModelScope.launch {
            val savedLocalFollowing = followingLocalDataSource.getAllAuthUserFollowings().first()
            val response = profileRepository.getFollowing(page = 1, limit = 3000, userId = userId)
            when (response) {
                is NetworkResult.Success -> {
                    val followersCount = response.data.followingCount
                    if (followersCount != savedLocalFollowing.size) {
                        response.data.following?.let {
                            followingLocalDataSource.deleteAllFollowing()
                            followingLocalDataSource.insertFollowings(followers = it)
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

    fun saveUserFollowers(userId: String) {
        viewModelScope.launch {
            val savedLocalFollowing = followersLocalDataSource.getAllAuthUserFollowers().first()
            val response = profileRepository.getFollowers(page = 1, limit = 3000, userId = userId)
            when (response) {
                is NetworkResult.Success -> {
                    val followersCount = response.data.followersCount
                    if (followersCount != savedLocalFollowing.size) {
                        response.data.followers?.let {
                            followersLocalDataSource.deleteAllFollowers()
                            followersLocalDataSource.insertFollowers(followers = it)
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
    fun deletePost(refresh:() -> Unit){
        viewModelScope.launch {
            val postId = _deletePostUiState.value.post?.postId ?: return@launch
            val response = postRepository.deletePostFromApi(postId = postId)
            when(response){
                is NetworkResult.Success -> {
                    if (response.data.success){
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Post deleted successfully"))
                        toggleDeletePostDialogState()
                        setPostToBeDeleted(null)
                        refresh()
                    }else{
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "An unexpected error occurred"))

                    }
                }
                is NetworkResult.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "An unexpected error occurred"))
                }
                is NetworkResult.Exception -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "An unexpected exception occurred"))
                }
            }
        }

    }

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
        ).flow.cachedIn(viewModelScope)
    }
}