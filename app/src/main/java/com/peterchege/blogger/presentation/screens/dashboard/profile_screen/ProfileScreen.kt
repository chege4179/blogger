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
package com.peterchege.blogger.presentation.screens.dashboard.profile_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.presentation.components.ArticleCard
import com.peterchege.blogger.presentation.components.BottomSheetItem
import com.peterchege.blogger.presentation.theme.lightGrayColor
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@ExperimentalCoilApi
@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    navigateToProfileFollowerFollowingScreen:(String) -> Unit,
    navigateToPostScreen:(String) -> Unit,
    navigateToLoginScreen:() -> Unit
) {

    val user = viewModel.user.collectAsStateWithLifecycle(initialValue = null)
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        sheetContent = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(lightGrayColor),
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
                            viewModel.logoutUser(navigateToLoginScreen = navigateToLoginScreen)
                        },
                        icon = Icons.Filled.Logout
                    )

                }
            }
        },
        sheetBackgroundColor = Color.White,
        sheetPeekHeight = 0.dp,


        ) {


        if (viewModel.isLoading.value) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            if (viewModel.isError.value) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = viewModel.msg.value
                    )

                }
            } else {
                user.value?.let { user ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .padding(10.dp),
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
                                            text = user.fullname,
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
                                            text = "${viewModel.posts.value.size}",
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
                                            text = "${user.followers.size}"
                                        )
                                        Text(
                                            text = "Followers",
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
                                            navigateToProfileFollowerFollowingScreen(Constants.FOLLOWING)

                                        },
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "${user.following?.size}",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                        )
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
                                                if (sheetState.isCollapsed) {
                                                    sheetState.expand()
                                                } else {
                                                    sheetState.collapse()
                                                }
                                            }
                                        }) {
                                        Text(text = "Settings")
                                    }

                                }
                            }
                            items(items = viewModel.posts.value) { post ->
                                ArticleCard(
                                    post = post,
                                    onItemClick = {
                                        navigateToPostScreen(post._id)

                                    },
                                    onProfileNavigate = { username ->

                                    },
                                    onDeletePost = {

                                    },
                                    profileImageUrl = user.imageUrl,
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

}