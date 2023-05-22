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
package com.peterchege.blogger.presentation.screens.dashboard.feed_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.core.util.categories
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.domain.state.FeedScreenUiState
import com.peterchege.blogger.presentation.components.ArticleCard
import com.peterchege.blogger.presentation.components.CategoryCard
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.components.ProfileCard
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest


@Composable
@ExperimentalCoilApi
fun FeedScreen(
    bottomNavController: NavController,
    navHostController: NavHostController,
    viewModel: FeedScreenViewModel = hiltViewModel()
){
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val authUser = viewModel.authUser.collectAsStateWithLifecycle()
    val networkStatus = viewModel.networkStatus.collectAsStateWithLifecycle()

    FeedScreenContent(
        bottomNavController = bottomNavController,
        navHostController = navHostController,
        uiState = uiState.value,
        authUser = authUser.value,
        eventFlow = viewModel.eventFlow,
        networkStatus =  networkStatus.value,
        retry = { viewModel.getFeedPosts() }
    )
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalCoilApi
fun FeedScreenContent(
    bottomNavController: NavController,
    navHostController: NavHostController,
    uiState: FeedScreenUiState,
    authUser:User?,
    eventFlow:SharedFlow<UiEvent>,
    networkStatus: NetworkStatus,
    retry:() -> Unit,

) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = networkStatus) {
        when (networkStatus) {
            is NetworkStatus.Unknown -> {}

            is NetworkStatus.Connected -> {}

            is NetworkStatus.Disconnected -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "You are offline"
                )
            }
        }
    }

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.Navigate -> {
                    navHostController.navigate(route = event.route)
                }
            }
        }
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.75f),
                        text = "Blogger"
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            navHostController.navigate(Screens.SEARCH_SCREEN)
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Chats",
                            Modifier.size(26.dp)

                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(Screens.ADD_NEW_POST_SCREEN)
                }
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                items(items = categories) { category ->
                    CategoryCard(navController = navHostController, categoryItem = category)
                    Spacer(modifier = Modifier.width(10.dp))

                }
            }
            when(uiState){
                is FeedScreenUiState.Error -> {
                    ErrorComponent(
                        errorMessage = uiState.message,
                        retryCallback = { retry() },
                    )
                }
                is FeedScreenUiState.Loading -> {
                    LoadingComponent()
                }
                is FeedScreenUiState.Success -> {
                    if (uiState.data.posts.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),

                            ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = "No posts were found ",
                                textAlign = TextAlign.Center,
                            )
                        }

                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp)
                        ) {

                            items(items = uiState.data.posts) { post ->
                                ArticleCard(
                                    post = post,
                                    onItemClick = {
                                        navHostController.navigate(Screens.POST_SCREEN + "/${post._id}/${Constants.API_SOURCE}")
                                    },
                                    onProfileNavigate = {
                                        onProfileNavigate(
                                            username = it,
                                            bottomNavController = bottomNavController,
                                            navHostController = navHostController,
                                            authUser = authUser,
                                        )
                                    },
                                    onDeletePost = {},
                                    isLiked = false,
                                    isSaved = false,
                                    isProfile = false,
                                    profileImageUrl = "https://res.cloudinary.com/dhuqr5iyw/image/upload/v1640971757/mystory/profilepictures/default_y4mjwp.jpg"
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                        }
                    }
                }
            }



        }
    }
}


fun onProfileNavigate(
    username: String,
    bottomNavController: NavController,
    authUser: User?,
    navHostController: NavHostController
) {
    val loginUsername = authUser?.username ?: ""
    if (loginUsername == username) {
        bottomNavController.navigate(Screens.PROFILE_NAVIGATION)
    } else {
        navHostController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/$username")
    }

}