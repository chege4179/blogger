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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.presentation.components.ArticleCard
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.theme.defaultPadding
import java.util.*


@Composable
fun AuthorProfileScreen(
    navigateToAuthorFollowerFollowingScreen: (String, String) -> Unit,
    navigateToPostScreen: (String) -> Unit,
    viewModel: AuthorProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()


    AuthorProfileScreenContent(
        followUser = {
            if (uiState is AuthorProfileScreenUiState.Success) {
                authUser?.let {
                    viewModel.followUser(
                        userToBeFollowed = (uiState as AuthorProfileScreenUiState.Success).user,
                        userFollowing = it
                    )
                }
            }
        },
        unfollowUser = {
            if (uiState is AuthorProfileScreenUiState.Success) {
                authUser?.let {
                    viewModel.unfollowUser(
                        userToBeUnfollowed = (uiState as AuthorProfileScreenUiState.Success).user,
                        userUnfollowing = it
                    )
                }
            }
        },
        uiState = uiState,
        navigateToAuthorFollowerFollowingScreen = navigateToAuthorFollowerFollowingScreen,
        navigateToPostScreen = navigateToPostScreen,
        authUser = authUser
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

    ) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
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
                val posts = uiState.posts
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
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
                                Image(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(80.dp)
                                        .align(Alignment.Center),
                                    painter = rememberImagePainter(
                                        data = user?.imageUrl,
                                        builder = {
                                            crossfade(true)

                                        },
                                    ),
                                    contentDescription = "Profile Image"
                                )

                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
//                                Text(
//                                    text = user?.fullname ?: "",
//                                    fontWeight = FontWeight.ExtraBold,
//                                    fontSize = 20.sp
//                                )
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
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Text(
                                    text = "${posts.size}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = "Articles",
                                    fontSize = 17.sp,
                                )
                            }
                            Divider(
                                color = Color.LightGray, thickness = 2.dp, modifier = Modifier
                                    .fillMaxHeight(0.7f)
                                    .width(1.dp)
                            )

                            Column(
                                modifier = Modifier.clickable {
                                    user?.username?.let { it1 ->
                                        navigateToAuthorFollowerFollowingScreen(
                                            it1,
                                            Constants.FOLLOWER
                                        )
                                    }
                                },
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    text = "${0}"
                                )
                                Text(
                                    text = "Followers",
                                    fontSize = 17.sp,

                                    )
                            }
                            Divider(
                                color = Color.LightGray, thickness = 2.dp, modifier = Modifier
                                    .fillMaxHeight(0.7f)
                                    .width(1.dp)
                            )

                            Column(
                                modifier = Modifier.clickable {
                                    user?.username?.let { it1 ->
                                        navigateToAuthorFollowerFollowingScreen(
                                            it1, Constants.FOLLOWING
                                        )
                                    }

                                },
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
//                                Text(
//                                    text = "${user?.following?.size}",
//                                    fontSize = 20.sp,
//                                    fontWeight = FontWeight.Bold,
//                                )
                                Text(
                                    text = "Following",
                                    fontSize = 17.sp,

                                    )
                            }

                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
//                            val authUserFollowing =
//                                authUser?.following?.map { it.followedId } ?: emptyList()
//                            if (authUserFollowing.contains(user?._id)) {
//                                Button(
//                                    modifier = Modifier.fillMaxWidth(0.5f),
//                                    onClick = {
//                                        unfollowUser()
//                                    }) {
//                                    Text(text = "Un Follow")
//                                }
//                            } else {
//                                Button(
//                                    modifier = Modifier.fillMaxWidth(0.5f),
//                                    onClick = {
//                                        followUser()
//                                    }) {
//                                    Text(text = "Follow")
//                                }
//                            }

                            Spacer(modifier = Modifier.width(5.dp))
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {

                                }) {
                                Text(text = "Message")
                            }

                        }
                    }
                    if (posts.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    text = "This user does not have any posts yet"
                                )
                            }
                        }
                    } else {
                        items(items = posts) { post ->
                            ArticleCard(
                                post = post,
                                onItemClick = {
                                    navigateToPostScreen(post.postId)
                                },
                                onProfileNavigate = { username ->

                                },
                                onDeletePost = {

                                },
                                isLiked = false,
                                isSaved = false,
                                isProfile = true
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                        }
                    }

                }

            }
        }
    }
}