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
package com.peterchege.blogger.presentation.screens.notifcations

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.peterchege.blogger.R
import com.peterchege.blogger.core.fake.dummyNotifications
import com.peterchege.blogger.core.util.isNotNull
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.components.NotificationCard
import com.peterchege.blogger.presentation.components.NotificationFilterCard
import com.peterchege.blogger.presentation.theme.defaultPadding
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun NotificationScreen(

    navigateToAuthorProfileScreen: (String) -> Unit,
    navigateToPostScreen: (String) -> Unit,
    viewModel: NotificationScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsStateWithLifecycle()
    val notificationFilter by viewModel.activeNotificationFilter.collectAsStateWithLifecycle()

    NotificationScreenContent(
        uiState = uiState,
        navigateToAuthorProfileScreen = navigateToAuthorProfileScreen,
        navigateToPostScreen = navigateToPostScreen,
        notificationFilter = notificationFilter,
        setNotificationFilter = viewModel::setNotificationFilter
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotificationScreenContent(
    uiState: NotificationScreenUiState,
    navigateToPostScreen: (String) -> Unit,
    navigateToAuthorProfileScreen: (String) -> Unit,
    notificationFilter: String,
    setNotificationFilter: (String) -> Unit,
) {
    val notificationFilters = listOf(
        "All",
        "Like",
        "Follower",
        "Comment",
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.notification_header),
                    )
                }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is NotificationScreenUiState.UserNotLoggedIn -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Text(
                        text = stringResource(id = R.string.login_to_view_notifications),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            is NotificationScreenUiState.Loading -> {
                LoadingComponent()
            }

            is NotificationScreenUiState.Error -> {
                ErrorComponent(
                    retryCallback = { /*TODO*/ },
                    errorMessage = uiState.message
                )
            }

            is NotificationScreenUiState.Success -> {
                val notifications = uiState.notifications.collectAsLazyPagingItems()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(defaultPadding)
                ) {
                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(items = notificationFilters, key = { it }) {
                                NotificationFilterCard(
                                    filterType = it,
                                    isActive = notificationFilter == it,
                                    setActiveFilter = setNotificationFilter
                                )
                            }
                        }
                    }

                    if (notifications.itemCount == 0) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,

                                ) {
                                Text(text = stringResource(id = R.string.no_notifications_message))
                            }
                        }
                    } else {
                        items(count = notifications.itemCount) { position ->
                            val notification = notifications[position]
                            val shouldShowNotification =
                                remember(key1 = notificationFilter,key2 = notification) {
                                    if (notificationFilter == "All")
                                        true
                                    else (notification?.notificationType == notificationFilter)
                                }
                            if(shouldShowNotification && notification.isNotNull()) {
                                NotificationCard(
                                    notification = notification!!,
                                    navigateToPostScreen = navigateToPostScreen,
                                    navigateToAuthorProfileScreen = navigateToAuthorProfileScreen,
                                )
                                Spacer(modifier = Modifier.padding(10.dp))
                            }

                        }
                    }
                    when(notifications.loadState.append){
                        is LoadState.Loading -> {
                            item {
                                LoadingComponent()
                            }

                        }
                        is LoadState.Error -> {

                        }
                        is LoadState.NotLoading -> {

                        }
                    }
                    when (notifications.loadState.prepend){
                        is LoadState.Loading -> {
                            item {
                                LoadingComponent()
                            }
                        }
                        is LoadState.NotLoading -> {
                            
                        }
                        is LoadState.Error ->  {

                        }
                    }

                }
            }
        }

    }
}

@Preview
@Composable
fun NotificationScreenPreview1(){
    NotificationScreenContent(
        uiState = NotificationScreenUiState.Success(
            notifications = flowOf(PagingData.from(dummyNotifications))
        ),
        navigateToPostScreen = {},
        navigateToAuthorProfileScreen = {},
        notificationFilter = "All",
        setNotificationFilter = {}
    )
}

@Preview
@Composable
fun NotificationScreenPreview2(){
    NotificationScreenContent(
        uiState = NotificationScreenUiState.Success(
            notifications = flowOf(PagingData.from(dummyNotifications))
        ),
        navigateToPostScreen = {},
        navigateToAuthorProfileScreen = {},
        notificationFilter = "Comment",
        setNotificationFilter = {}
    )
}