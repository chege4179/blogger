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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.presentation.components.ArticleCard
import com.peterchege.blogger.presentation.components.BottomSheetItem
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.components.NotLoggedInComponent
import com.peterchege.blogger.presentation.components.PagingLoader
import com.peterchege.blogger.presentation.theme.defaultPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    navigateToProfileFollowerFollowingScreen: (String) -> Unit,
    navigateToPostScreen: (String) -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
) {

    val authUser by viewModel.authUser.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val networkStatus by viewModel.networkStatus.collectAsStateWithLifecycle()

    ProfileScreenContent(
        networkStatus = networkStatus,
        user = authUser,
        uiState = uiState,
        navigateToProfileFollowerFollowingScreen = navigateToProfileFollowerFollowingScreen,
        navigateToPostScreen = navigateToPostScreen,
        navigateToLoginScreen = navigateToLoginScreen,
        navigateToSignUpScreen = navigateToSignUpScreen,
        logoutUser = { authUser?.let { viewModel.logoutUser(navigateToLoginScreen, it) } }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalCoilApi
@Composable
fun ProfileScreenContent(
    networkStatus: NetworkStatus,
    user: User?,
    uiState: ProfileScreenUiState,
    navigateToProfileFollowerFollowingScreen: (String) -> Unit,
    navigateToPostScreen: (String) -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateToSignUpScreen:() -> Unit,
    logoutUser: () -> Unit,
) {

    val snackbarHostState = SnackbarHostState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
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
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),

        ) { _ ->
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
                Text(text = "User Not found")

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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
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
                                            data = user.imageUrl,
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
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally

                                ) {
                                    Text(
                                        text = "${user._count.post}",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = "Articles",
                                        fontSize = 17.sp,
                                    )
                                }
                                Divider(
                                    color = Color.LightGray,
                                    thickness = 2.dp,
                                    modifier = Modifier
                                        .fillMaxHeight(0.7f)
                                        .width(1.dp)
                                )
                                Column(
                                    modifier = Modifier.clickable {
                                        navigateToProfileFollowerFollowingScreen(Constants.FOLLOWER)

                                    },
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        text = "${user._count.followers}"
                                    )
                                    Text(
                                        text = "Followers",
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
                                Button(
                                    modifier = Modifier.fillMaxWidth(0.5f),
                                    onClick = {

                                    }) {
                                    Text(text = "Edit Profile")
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        scope.launch {
                                            if (sheetState.isVisible) {
                                                sheetState.hide()
                                            } else {
                                                sheetState.expand()
                                            }
                                        }
                                    }) {
                                    Text(text = "Settings")
                                }

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

        if (sheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = { /*TODO*/ },
                sheetState = sheetState,
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .fillMaxWidth()
                        .height(350.dp)
                        ,
                    contentAlignment = Alignment.Center,

                    ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        BottomSheetItem(
                            name = "Account",
                            onClick = {

                            },
                            icon = Icons.Filled.Person
                        )
                        BottomSheetItem(
                            name = "Notification Settings",
                            onClick = {

                            },
                            icon = Icons.Filled.Settings
                        )
                        BottomSheetItem(
                            name = "Help",
                            onClick = {

                            },
                            icon = Icons.Filled.Help
                        )
                        BottomSheetItem(
                            name = "Share",
                            onClick = {

                            },
                            icon = Icons.Filled.Share
                        )
                        BottomSheetItem(
                            name = "Log Out",
                            onClick = {
                                logoutUser()
                            },
                            icon = Icons.Filled.Logout
                        )

                    }
                }
            }

        }
    }

}