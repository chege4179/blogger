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

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.core.util.categories
import com.peterchege.blogger.domain.mappers.toPost
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.presentation.components.ArticleCard
import com.peterchege.blogger.presentation.components.CategoryCard
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.theme.defaultPadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
@ExperimentalCoilApi
fun FeedScreen(
    navigateToPostScreen: (String) -> Unit,
    navigateToAuthUserProfileScreen: () -> Unit,
    navigateToAuthorProfileScreen: (String) -> Unit,
    navigateToSearchScreen: () -> Unit,
    navigateToAddPostScreen: () -> Unit,
    navigateToCategoryScreen: (String) -> Unit,
    viewModel: FeedScreenViewModel = hiltViewModel()
) {
//    val wakeLockPermissionState = rememberPermissionState(permission = android.Manifest.permission.WAKE_LOCK)
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()
    val networkStatus by viewModel.networkStatus.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val feedScreenUiState by viewModel.feedScreenUiState.collectAsStateWithLifecycle()

    val pullRefreshState = rememberPullToRefreshState()
    LaunchedEffect(key1 = pullRefreshState.isRefreshing) {
        viewModel.refreshFeed()
        delay(1500)
    }

    FeedScreenContent(
        navigateToPostScreen = navigateToPostScreen,
        navigateToAuthUserProfileScreen = navigateToAuthUserProfileScreen,
        navigateToAuthorProfileScreen = navigateToAuthorProfileScreen,
        navigateToSearchScreen = navigateToSearchScreen,
        navigateToAddPostScreen = navigateToAddPostScreen,
        navigateToCategoryScreen = navigateToCategoryScreen,
        authUser = authUser,
        eventFlow = viewModel.eventFlow,
        networkStatus = networkStatus,
        pullRefreshState = pullRefreshState,
        uiState = feedScreenUiState,
        isRefreshing = isSyncing,
        retryCallback = viewModel::refreshFeed,
        bookmarkPost = viewModel::bookmarkPost,
        unBookmarkPost = viewModel::unBookmarkPost
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalCoilApi
fun FeedScreenContent(
    navigateToPostScreen: (String) -> Unit,
    navigateToAuthUserProfileScreen: () -> Unit,
    navigateToAuthorProfileScreen: (String) -> Unit,
    navigateToAddPostScreen: () -> Unit,
    navigateToSearchScreen: () -> Unit,
    navigateToCategoryScreen: (String) -> Unit,
    isRefreshing: Boolean,
    authUser: User?,
    uiState: FeedScreenUiState,
    eventFlow: SharedFlow<UiEvent>,
    networkStatus: NetworkStatus,
    pullRefreshState: PullToRefreshState,
    retryCallback: () -> Unit,
    bookmarkPost: (Post) -> Unit,
    unBookmarkPost: (Post) -> Unit,

    ) {
    val snackbarHostState = SnackbarHostState()
    LaunchedEffect(key1 = networkStatus) {
        when (networkStatus) {
            is NetworkStatus.Unknown -> {}

            is NetworkStatus.Connected -> {}

            is NetworkStatus.Disconnected -> {
                snackbarHostState.showSnackbar(
                    message = "You are offline"
                )
            }
        }
    }

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

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.75f),
                        text = "Blogger "
                    )
                },
                actions = {

                    IconButton(
                        onClick = {
                            navigateToSearchScreen()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Posts",
                            modifier = Modifier.size(26.dp)

                        )
                    }
                },
                scrollBehavior = scrollBehavior

            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    navigateToAddPostScreen()

                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Create Post"
                )

            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it)
                .nestedScroll(pullRefreshState.nestedScrollConnection)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items(items = categories) { category ->
                        CategoryCard(
                            modifier = Modifier.height(30.dp),
                            navigateToCategoryScreen = navigateToCategoryScreen,
                            categoryItem = category
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                    }
                }
                when (uiState) {
                    is FeedScreenUiState.Empty -> {
                        ErrorComponent(
                            retryCallback = { retryCallback() },
                            errorMessage = "No posts were found"
                        )
                    }

                    is FeedScreenUiState.Loading -> {
                        LoadingComponent()
                    }

                    is FeedScreenUiState.Error -> {
                        ErrorComponent(
                            retryCallback = { retryCallback() },
                            errorMessage = uiState.message
                        )
                    }

                    is FeedScreenUiState.Success -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(defaultPadding)
                        ) {

                            items(items = uiState.posts, key = { it.postId }) { post ->
                                ArticleCard(
                                    post = post.toPost(),
                                    onItemClick = { post ->
                                        navigateToPostScreen(post.postId)
                                    },
                                    onProfileNavigate = { username ->
                                        navigateToAuthorProfileScreen(username)
                                    },
                                    onDeletePost = {},
                                    isLiked = post.isLiked,
                                    isSaved = post.isSaved,
                                    isProfile = false,
                                    onBookmarkPost = { post ->
                                        bookmarkPost(post)
                                    },
                                    onUnBookmarkPost = { post ->
                                        unBookmarkPost(post)
                                    },
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                        }
                    }
                }
            }
            PullToRefreshContainer(
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }


    }
}