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

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.peterchege.blogger.R
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.core.util.toast
import com.peterchege.blogger.presentation.components.ArticleCard
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.FollowButtonSection
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.components.PagingLoader
import com.peterchege.blogger.presentation.components.ProfileAvatar
import com.peterchege.blogger.presentation.components.ProfileInfoCount
import com.peterchege.blogger.presentation.theme.defaultPadding
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.util.*


@Composable
fun AuthorProfileScreen(
    navigateToAuthorFollowerFollowingScreen: (String, String) -> Unit,
    navigateToPostScreen: (String) -> Unit,
    viewModel: AuthorProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()
    val likedPostIds by viewModel.likedPostIds.collectAsStateWithLifecycle()
    val savedPostIds by viewModel.savedPostIds.collectAsStateWithLifecycle()


    AuthorProfileScreenContent(
        uiState = uiState,
        navigateToAuthorFollowerFollowingScreen = navigateToAuthorFollowerFollowingScreen,
        navigateToPostScreen = navigateToPostScreen,
        authUser = authUser,
        savedPostIds = savedPostIds,
        likedPostIds = likedPostIds,
        bookmarkPost = viewModel::bookmarkPost,
        unBookmarkPost = viewModel::unBookmarkPost,
        eventFlow = viewModel.eventFlow,
        likePost = { post ->
            if (authUser == null){
                context.toast(msg = context.getString(R.string.login_like_message))
            }
            authUser?.let { user ->
                if (user.userId != "") {
                    viewModel.likePost(post = post, user = user)
                }else{
                    context.toast(msg = context.getString(R.string.login_like_message))
                }
            }
        },
        unLikePost = { post ->
            if (authUser == null){
                context.toast(msg = context.getString(R.string.login_like_message))
            }
            authUser?.let { user ->
                if (user.userId != "") {
                    viewModel.unLikePost(post = post, user = user)
                }else{
                    context.toast(msg = context.getString(R.string.login_like_message))
                }
            }
        },
        followUser = {
            if (uiState is AuthorProfileScreenUiState.Success) {
                authUser?.let {
                    viewModel.followUser(
                        userIdToBeFollowed = (uiState as AuthorProfileScreenUiState.Success).user.userId,
                        userId = it.userId
                    )
                }
            }
        },
        unfollowUser = {
            if (uiState is AuthorProfileScreenUiState.Success) {
                authUser?.let {
                    viewModel.unfollowUser(
                        unfollowedUserId = (uiState as AuthorProfileScreenUiState.Success).user.userId,
                        userId = it.userId
                    )
                }
            }
        },
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalCoilApi::class)
@Composable
fun AuthorProfileScreenContent(
    authUser: User?,
    followUser: () -> Unit,
    unfollowUser: () -> Unit,
    uiState: AuthorProfileScreenUiState,
    navigateToAuthorFollowerFollowingScreen: (String, String) -> Unit,
    navigateToPostScreen: (String) -> Unit,
    savedPostIds: List<String>,
    likedPostIds: List<String>,
    eventFlow: SharedFlow<UiEvent>,
    bookmarkPost: (Post) -> Unit,
    unBookmarkPost: (Post) -> Unit,
    likePost: (Post) -> Unit,
    unLikePost: (Post) -> Unit,

    ) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.Navigate -> {

                }
            }
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        when (uiState) {
            is AuthorProfileScreenUiState.Loading -> {
                LoadingComponent()
            }

            is AuthorProfileScreenUiState.Error -> {
                ErrorComponent(
                    errorMessage = uiState.message,
                    retryCallback = { },
                )

            }

            is AuthorProfileScreenUiState.Success -> {
                val user = uiState.user
                val posts = uiState.posts.collectAsLazyPagingItems()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(defaultPadding),
                ) {

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.3f)
                            ) {
                                ProfileAvatar(
                                    size = 80,
                                    imageUrl = user.imageUrl,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = user.fullName,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.padding(3.dp))
                                Text(
                                    text = "@" + (user?.username?.toLowerCase(Locale.ROOT)
                                        ?: ""),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))


                            }
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ProfileInfoCount(
                                name = stringResource(id = R.string.post_header_name),
                                count = user.count.post,
                                onClick = {

                                }
                            )
                            VerticalDivider(
                                modifier = Modifier
                                    .fillMaxHeight(0.7f)
                                    .width(1.dp),
                                thickness = 2.dp,
                                color = Color.LightGray
                            )
                            ProfileInfoCount(
                                name = stringResource(id = R.string.followers_header_name),
                                count = user.count.followers,
                                onClick = {
                                    navigateToAuthorFollowerFollowingScreen(
                                        user.userId,
                                        Constants.FOLLOWER
                                    )
                                }
                            )
                            VerticalDivider(
                                modifier = Modifier
                                    .fillMaxHeight(0.7f)
                                    .width(1.dp),
                                thickness = 2.dp,
                                color = Color.LightGray
                            )
                            ProfileInfoCount(
                                name = stringResource(id = R.string.following_header_name),
                                count = user.count.following,
                                onClick = {
                                    navigateToAuthorFollowerFollowingScreen(
                                        user.userId,
                                        Constants.FOLLOWING
                                    )
                                }
                            )
                        }
                    }

                    if (uiState.isUserLoggedIn) {
                        item {
                            FollowButtonSection(
                                followUser = followUser,
                                unfollowUser = unfollowUser,
                                isFollowingMe = uiState.isFollowingMe,
                                isAuthUserFollowingBack = uiState.isAuthUserFollowingBack
                            )
                        }
                    }
                    if (posts.itemCount == 0) {
                        item {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    text = stringResource(id = R.string.empty_posts_message)
                                )
                            }
                        }
                    } else {
                        items(count = posts.itemCount) { position ->
                            val post = posts[position]
                            if (post != null) {
                                ArticleCard(
                                    post = post,
                                    isLiked = likedPostIds.contains(post.postId),
                                    isSaved = savedPostIds.contains(post.postId),
                                    isProfile = false,
                                    onItemClick = {
                                        navigateToPostScreen(post.postId)
                                    },
                                    onProfileNavigate = {},
                                    onDeletePost = {},
                                    onBookmarkPost = bookmarkPost,
                                    onUnBookmarkPost = unBookmarkPost,
                                    onLikePost = likePost,
                                    onUnlikePost = unLikePost

                                )
                            }
                            Spacer(modifier = Modifier.padding(5.dp))
                        }
                    }
                    if (posts.loadState.prepend is LoadState.Loading) {
                        item {
                            PagingLoader()
                        }
                    }
                }

            }
        }
    }
}