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
package com.peterchege.blogger.presentation.screens.search.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.presentation.components.ArticleCard
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.screens.search.SearchScreenUiState
import com.peterchege.blogger.R


@OptIn(ExperimentalCoilApi::class)
@Composable
fun SearchPostsTab(
    navigateToPostScreen: (String) -> Unit,
    onRetry:() -> Unit,
    uiState: SearchScreenUiState,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        when (uiState) {
            is SearchScreenUiState.Idle -> {

            }

            is SearchScreenUiState.Searching -> {
                LoadingComponent()
            }

            is SearchScreenUiState.Error -> {
                ErrorComponent(
                    retryCallback = onRetry,
                    errorMessage = uiState.message
                )
            }

            is SearchScreenUiState.ResultsFound -> {
                val searchPosts = uiState.posts
                if (searchPosts.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(id = R.string.no_posts_found))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp)
                        ,
                    ) {
                        items(items = searchPosts) { post ->
                            ArticleCard(
                                post = post,
                                onItemClick = {
                                    navigateToPostScreen(post.postId)

                                },
                                onProfileNavigate = {},
                                onDeletePost = {},
                                isLiked = false,
                                isSaved = false,
                                isProfile = false,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}