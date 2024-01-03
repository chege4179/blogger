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
package com.peterchege.blogger.presentation.screens.saved_posts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.presentation.components.ArticleCard
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.theme.defaultPadding

@Composable
fun SavedPostScreen(
    navigateToPostScreen:(String) -> Unit,
    viewModel: SavedPostScreenViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SavedPostScreenContent(
        navigateToPostScreen = navigateToPostScreen,
        uiState = uiState
    )
}



@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SavedPostScreenContent(
    navigateToPostScreen:(String) -> Unit,
    uiState: SavedPostScreenUiState,
){
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text= "Your saved posts",
                    )
                },
            )
        }
    ) { paddingValues ->
        when(uiState){
            is SavedPostScreenUiState.Loading -> {
                LoadingComponent()
            }
            is SavedPostScreenUiState.Empty -> {

            }
            is SavedPostScreenUiState.Error -> {
                ErrorComponent(
                    retryCallback = { /*TODO*/ },
                    errorMessage = uiState.message)
            }
            is SavedPostScreenUiState.Success -> {
                val posts = uiState.posts
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(defaultPadding)
                    ,

                ){
                    if (posts.isEmpty()){
                        item{
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Text(
                                    text = "You have no saved posts",
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }

                    }
                    items(items = posts, key = { it.postId }){ post ->
                        ArticleCard(
                            post = post,
                            onItemClick = {
                                navigateToPostScreen(it.postId)
                            },
                            onProfileNavigate = {

                            },
                            onDeletePost = {

                            },
                            isLiked = false,
                            isSaved =true,
                            isProfile = false
                        )
                    }
                }
            }
        }

    }


}