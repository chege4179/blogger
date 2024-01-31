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

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberImagePainter
import com.peterchege.blogger.R
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.presentation.alertDialogs.DeletePostDialog
import com.peterchege.blogger.presentation.components.ArticleCard
import com.peterchege.blogger.presentation.components.BottomSheetItem
import com.peterchege.blogger.presentation.components.CustomIconButton
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.components.NotLoggedInComponent
import com.peterchege.blogger.presentation.components.PagingLoader
import com.peterchege.blogger.presentation.components.ProfileAvatar
import com.peterchege.blogger.presentation.components.ProfileInfoCount
import com.peterchege.blogger.presentation.theme.defaultPadding
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    navigateToProfileFollowerFollowingScreen: (String) -> Unit,
    navigateToPostScreen: (String) -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
    navigateToEditProfileScreen: () -> Unit,
    navigateToEditPostScreen: (Post) -> Unit,
) {
    val deletePostUiState by viewModel.deletePostUiState.collectAsStateWithLifecycle()
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val networkStatus by viewModel.networkStatus.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = authUser) {
        authUser?.let {
            if (it.userId != "") {
                viewModel.saveUserLikes(userId = it.userId)
                viewModel.saveUserFollowing(userId = it.userId)
                viewModel.saveUserFollowers(userId = it.userId)
            }
        }
    }
    ProfileScreenContent(
        deletePostUiState = deletePostUiState,
        networkStatus = networkStatus,
        uiState = uiState,
        eventFlow = viewModel.eventFlow,
        navigateToProfileFollowerFollowingScreen = navigateToProfileFollowerFollowingScreen,
        navigateToPostScreen = navigateToPostScreen,
        navigateToLoginScreen = navigateToLoginScreen,
        navigateToSignUpScreen = navigateToSignUpScreen,
        navigateToSettingsScreen = navigateToSettingsScreen,
        navigateToEditProfileScreen = navigateToEditProfileScreen,
        navigateToEditPostScreen = navigateToEditPostScreen,
        toggleDeletePostDialog = viewModel::toggleDeletePostDialogState,
        setDeletePost = viewModel::setPostToBeDeleted,
        deletePost = {
            viewModel.deletePost { it() }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalCoilApi
@Composable
fun ProfileScreenContent(
    deletePostUiState: DeletePostUiState,
    toggleDeletePostDialog: () -> Unit,
    setDeletePost: (Post) -> Unit,
    networkStatus: NetworkStatus,
    uiState: ProfileScreenUiState,
    navigateToProfileFollowerFollowingScreen: (String) -> Unit,
    navigateToPostScreen: (String) -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
    navigateToEditProfileScreen: () -> Unit,
    navigateToEditPostScreen:(Post) -> Unit,
    deletePost:(() -> Unit) -> Unit,
    eventFlow:SharedFlow<UiEvent>
    ) {

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()
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
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    CustomIconButton(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit_profile_description),
                        onClick = { navigateToEditProfileScreen() },
                    )
                    CustomIconButton(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(id = R.string.settings_description),
                        onClick = { navigateToSettingsScreen() },
                    )
                }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is ProfileScreenUiState.Loading -> {
                LoadingComponent()
            }

            is ProfileScreenUiState.Error -> {
                ErrorComponent(
                    retryCallback = { /*TODO*/ },
                    errorMessage = uiState.message
                )
            }

            is ProfileScreenUiState.Empty -> {
                Text(text = stringResource(id = R.string.user_not_found))

            }

            is ProfileScreenUiState.UserNotLoggedIn -> {
                NotLoggedInComponent(
                    navigateToLoginScreen = navigateToLoginScreen,
                    navigateToSignUpScreen = navigateToSignUpScreen,
                )
            }

            is ProfileScreenUiState.Success -> {
                val posts = uiState.posts.collectAsLazyPagingItems()
                val user = uiState.user
                if (deletePostUiState.isDeletePostDialogOpen && deletePostUiState.post != null) {
                    DeletePostDialog(
                        post = deletePostUiState.post,
                        closeDeleteDialog = toggleDeletePostDialog,
                        deletePost = { deletePost{ posts.refresh() } }
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.background)
                ) {
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
                                        text = "@" + user.username,
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
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                ProfileInfoCount(
                                    name = stringResource(id = R.string.followers_header_name),
                                    count = user.count.followers,
                                    onClick = {
                                        navigateToProfileFollowerFollowingScreen(Constants.FOLLOWER)
                                    }
                                )
                                VerticalDivider(
                                    modifier = Modifier
                                        .fillMaxHeight(0.7f)
                                        .width(1.dp),
                                    thickness = 2.dp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                ProfileInfoCount(
                                    name = stringResource(id = R.string.following_header_name),
                                    count = user.count.following,
                                    onClick = {
                                        navigateToProfileFollowerFollowingScreen(Constants.FOLLOWING)
                                    }
                                )

                            }
                        }
                        items(count = posts.itemCount) { position ->
                            val post = posts[position]
                            if (post != null) {
                                ArticleCard(
                                    post = post,
                                    onItemClick = {
                                        navigateToPostScreen(post.postId)
                                    },
                                    navigateToEditPostScreen = navigateToEditPostScreen,
                                    openDeleteDialog = {
                                        setDeletePost(it)
                                        toggleDeletePostDialog()
                                    },
                                    onProfileNavigate = { username ->

                                    },
                                    onDeletePost = {

                                    },
                                    isLiked = false,
                                    isSaved = false,
                                    isProfile = true
                                )
                            }
                            Spacer(modifier = Modifier.padding(5.dp))
                        }
                        if (posts.loadState.append is LoadState.Loading) {
                            item {
                                PagingLoader()
                            }
                        }
                    }
                }
            }
        }
    }
}