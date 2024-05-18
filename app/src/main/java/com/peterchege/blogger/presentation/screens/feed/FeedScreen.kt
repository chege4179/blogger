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
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.core.util.categories
import com.peterchege.blogger.core.util.toast
import com.peterchege.blogger.domain.mappers.toPost
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.presentation.components.ArticleCard
import com.peterchege.blogger.presentation.components.CategoryCard
import com.peterchege.blogger.presentation.components.CustomIconButton
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.theme.defaultPadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import com.peterchege.blogger.R
import com.peterchege.blogger.core.fake.dummyPostList
import com.peterchege.blogger.core.util.setTagAndId
import com.peterchege.blogger.domain.models.PostUI
import com.peterchege.blogger.presentation.components.AppBackgroundImage
import com.peterchege.blogger.presentation.theme.BloggerTheme
import kotlinx.coroutines.flow.MutableSharedFlow


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
    val context = LocalContext.current
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
        eventFlow = viewModel.eventFlow,
        networkStatus = networkStatus,
        pullRefreshState = pullRefreshState,
        uiState = feedScreenUiState,
        retryCallback = viewModel::refreshFeed,
        bookmarkPost = viewModel::bookmarkPost,
        unBookmarkPost = viewModel::unBookmarkPost,
        likePost = { post ->
            if (authUser == null) {
                context.toast(msg = context.getString(R.string.login_like_message))
            }
            authUser?.let { user ->
                if (user.userId != "") {
                    viewModel.likePost(post = post, user = user)
                } else {
                    context.toast(msg = context.getString(R.string.login_like_message))
                }
            }
        },
        unLikePost = { post ->
            if (authUser == null) {
                context.toast(msg = context.getString(R.string.login_like_message))
            }
            authUser?.let { user ->
                if (user.userId != "") {
                    viewModel.unLikePost(post = post, user = user)
                } else {
                    context.toast(msg = context.getString(R.string.login_like_message))
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
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
    uiState: FeedScreenUiState,
    eventFlow: SharedFlow<UiEvent>,
    networkStatus: NetworkStatus,
    pullRefreshState: PullToRefreshState,
    retryCallback: () -> Unit,
    bookmarkPost: (Post) -> Unit,
    unBookmarkPost: (Post) -> Unit,
    likePost: (Post) -> Unit,
    unLikePost: (Post) -> Unit,

    ) {
    val snackbarHostState = remember { SnackbarHostState() }

    val offlineMessage = stringResource(id = R.string.offline_message)

    LaunchedEffect(key1 = networkStatus) {
        when (networkStatus) {
            is NetworkStatus.Unknown -> {}

            is NetworkStatus.Connected -> {}

            is NetworkStatus.Disconnected -> {
                snackbarHostState.showSnackbar(
                    message = offlineMessage
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
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .semantics {
                testTagsAsResourceId = true
            },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.75f),
                        text = stringResource(id = R.string.app_name)
                    )
                },
                actions = {
                    CustomIconButton(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_icon_description),
                        onClick = navigateToSearchScreen
                    )
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
                    contentDescription = stringResource(id = R.string.create_post_icon_description)
                )

            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(paddingValues = paddingValues)
                .nestedScroll(pullRefreshState.nestedScrollConnection)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(5.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    items(items = categories) { category ->
//                        CategoryCard(
//                            modifier = Modifier.height(30.dp),
//                            navigateToCategoryScreen = navigateToCategoryScreen,
//                            categoryItem = category
//                        )
//                        Spacer(modifier = Modifier.width(10.dp))
//
//                    }
//                }
                AnimatedContent(targetState = uiState, label = "Feed Screen" ) { uiState ->
                    when (uiState) {
                        is FeedScreenUiState.Empty -> {
                            ErrorComponent(
                                retryCallback = { retryCallback() },
                                errorMessage = stringResource(id = R.string.no_posts_found)
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

                                    .setTagAndId("feed")
                                    .fillMaxSize()
                                    .padding(defaultPadding)
                            ) {

                                items(items = uiState.posts, key = { it.postId }) { post ->
                                    ArticleCard(
                                        post = post.toPost(),
                                        onItemClick = { post ->
                                            navigateToPostScreen(post.postId)
                                        },
                                        onProfileNavigate = { userId ->
                                            navigateToAuthorProfileScreen(userId)
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
                                        onLikePost = {
                                            likePost(it)
                                        },
                                        onUnlikePost = {
                                            unLikePost(it)

                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FeedScreenPreviewLightTheme() {
    BloggerTheme {
        AppBackgroundImage()
        FeedScreenContent(
            navigateToPostScreen = {},
            navigateToAuthUserProfileScreen = { /*TODO*/ },
            navigateToAuthorProfileScreen = {},
            navigateToAddPostScreen = { /*TODO*/ },
            navigateToSearchScreen = { /*TODO*/ },
            navigateToCategoryScreen = {},
            uiState = FeedScreenUiState.Success(posts = dummyPostList),
            eventFlow = MutableSharedFlow(),
            networkStatus = NetworkStatus.Connected,
            pullRefreshState = rememberPullToRefreshState(),
            retryCallback = { /*TODO*/ },
            bookmarkPost = {},
            unBookmarkPost = {},
            likePost = {},
            unLikePost = {}
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Preview
@Composable
fun FeedScreenPreviewDarkTheme() {
    BloggerTheme(darkTheme = true) {
        FeedScreenContent(
            navigateToPostScreen = {},
            navigateToAuthUserProfileScreen = { /*TODO*/ },
            navigateToAuthorProfileScreen = {},
            navigateToAddPostScreen = { /*TODO*/ },
            navigateToSearchScreen = { /*TODO*/ },
            navigateToCategoryScreen = {},
            uiState = FeedScreenUiState.Success(posts = dummyPostList),
            eventFlow = MutableSharedFlow(),
            networkStatus = NetworkStatus.Connected,
            pullRefreshState = rememberPullToRefreshState(),
            retryCallback = { /*TODO*/ },
            bookmarkPost = {},
            unBookmarkPost = {},
            likePost = {},
            unLikePost = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(device = "spec:id=reference_tablet,shape=Normal,width=1280,height=800,unit=dp,dpi=240")
@Composable
fun FeedScreenPreviewTablet() {
    BloggerTheme {
        FeedScreenContent(
            navigateToPostScreen = {},
            navigateToAuthUserProfileScreen = { /*TODO*/ },
            navigateToAuthorProfileScreen = {},
            navigateToAddPostScreen = { /*TODO*/ },
            navigateToSearchScreen = { /*TODO*/ },
            navigateToCategoryScreen = {},
            uiState = FeedScreenUiState.Success(posts = dummyPostList),
            eventFlow = MutableSharedFlow(),
            networkStatus = NetworkStatus.Connected,
            pullRefreshState = rememberPullToRefreshState(),
            retryCallback = { /*TODO*/ },
            bookmarkPost = {},
            unBookmarkPost = {},
            likePost = {},
            unLikePost = {}
        )
    }

}