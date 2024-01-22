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
package com.peterchege.blogger.presentation.screens.search

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.presentation.screens.search.tabs.SearchPostsTab
import com.peterchege.blogger.presentation.screens.search.tabs.SearchUsersTab
import com.peterchege.blogger.presentation.theme.MainWhiteColor
import kotlinx.coroutines.launch
import com.peterchege.blogger.R

@Composable
fun SearchScreen(
    navigateToPostScreen: (String) -> Unit,
    navigateToAuthorProfileScreen: (String) -> Unit,

    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val networkStatus by viewModel.networkStatus.collectAsStateWithLifecycle()
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()


    SearchScreenContent(
        uiState = uiState,
        searchQuery = searchQuery,
        onChangeSearchQuery = viewModel::onChangeSearchTerm,
        navigateToPostScreen = navigateToPostScreen,
        navigateToAuthorProfileScreen = navigateToAuthorProfileScreen,
        networkStatus = networkStatus,
    )

}


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreenContent(

    uiState: SearchScreenUiState,
    searchQuery: String,
    onChangeSearchQuery: (String) -> Unit,
    navigateToPostScreen: (String) -> Unit,
    navigateToAuthorProfileScreen: (String) -> Unit,
    networkStatus: NetworkStatus,
) {


    val snackbarHostState = SnackbarHostState()

    LaunchedEffect(key1 = networkStatus) {
        when (networkStatus) {
            is NetworkStatus.Unknown -> {

            }

            is NetworkStatus.Disconnected -> {
                snackbarHostState.showSnackbar(message = "Not connected")
            }

            is NetworkStatus.Connected -> {}
        }

    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        onChangeSearchQuery(it)
                    },
                    placeholder = {
                        Text(
                            text = "Search",
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .clickable {
                        },
                    shape = RoundedCornerShape(size = 8.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        autoCorrect = true,
                        keyboardType = KeyboardType.Text,
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = MainWhiteColor,
                        focusedIndicatorColor = MainWhiteColor,
                        unfocusedIndicatorColor = MainWhiteColor,
                        disabledIndicatorColor = MainWhiteColor
                    ),
                    textStyle = TextStyle(color = Color.Black),
                    maxLines = 1,
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {

                                }
                        )
                    }
                )
            }
        },
    ) { paddingValues ->
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = { 2 }
        )
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(paddingValues)
                .padding(vertical = 5.dp),
        ) {
            Tabs(pagerState = pagerState)
            TabsContent(
                pagerState = pagerState,
                uiState = uiState,
                navigateToPostScreen = navigateToPostScreen,
                navigateToAuthorProfileScreen = navigateToAuthorProfileScreen,
            )
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        stringResource(id = R.string.post_tab),
        stringResource(id = R.string.user_tab)
    )
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                height = 2.5.dp
            )
        }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        text = list[index],
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = if (pagerState.currentPage == index)
                            MaterialTheme.colorScheme.primary else Color.LightGray
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(
    pagerState: PagerState,
    uiState: SearchScreenUiState,
    navigateToAuthorProfileScreen: (String) -> Unit,
    navigateToPostScreen: (String) -> Unit,
) {
    HorizontalPager(state = pagerState) {
        AnimatedContent(
            targetState = pagerState,
            label = "Horizontal Pager",

            ) { pager ->
            when (pager.currentPage) {
                0 -> SearchPostsTab(
                    uiState = uiState,
                    navigateToPostScreen = navigateToPostScreen
                )

                1 -> SearchUsersTab(
                    uiState = uiState,
                    navigateToAuthorProfileScreen = navigateToAuthorProfileScreen
                )

            }
        }

    }
}
