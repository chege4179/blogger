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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.core.util.categories
import com.peterchege.blogger.presentation.components.ArticleCard
import com.peterchege.blogger.presentation.components.CategoryCard
import com.peterchege.blogger.presentation.components.ProfileCard

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalCoilApi
fun FeedScreen(
    bottomNavController: NavController,
    navHostController: NavHostController,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = viewModel.isError.value) {
        if (viewModel.isError.value) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = viewModel.errorMsg.value
            )
        }
    }
    val state = viewModel.state.value
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
                            Icons.Filled.Search,
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
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
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
                    items(categories) { category ->
                        CategoryCard(navController = navHostController, categoryItem = category)
                        Spacer(modifier = Modifier.width(10.dp))

                    }
                }
                if (!viewModel.isFound.value) {
                    Box(
                        modifier = Modifier.fillMaxSize(),

                        ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "No posts or users were found ... Please try a different search",
                            textAlign = TextAlign.Center,
                        )
                    }

                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp)
                    ) {

                        items(state.posts) { post ->
                            ArticleCard(
                                post = post,
                                onItemClick = {
                                    navHostController.navigate(Screens.POST_SCREEN + "/${post._id}/${Constants.API_SOURCE}")
                                },
                                onProfileNavigate = {
                                    viewModel.onProfileNavigate(
                                        it,
                                        bottomNavController,
                                        navHostController
                                    )
                                },
                                onDeletePost = {

                                },
                                isLiked = false,
                                isSaved = false,
                                isProfile = false,
                                profileImageUrl = "https://res.cloudinary.com/dhuqr5iyw/image/upload/v1640971757/mystory/profilepictures/default_y4mjwp.jpg"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(state.users) { user ->
                            ProfileCard(
                                navController = navHostController,
                                user = user,
                                onProfileNavigate = {
                                    viewModel.onProfileNavigate(
                                        it,
                                        bottomNavController,
                                        navHostController
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}