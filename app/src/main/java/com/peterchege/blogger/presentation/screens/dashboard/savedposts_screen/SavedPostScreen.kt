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
package com.peterchege.blogger.presentation.screens.dashboard.savedposts_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.presentation.components.ArticleCard
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.domain.mappers.toExternalModel
import com.peterchege.blogger.presentation.navigation.navigateToPostScreen

@OptIn(ExperimentalCoilApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SavedPostScreen(
    navigateToPostScreen:(String) -> Unit,
    viewModel: SavedPostScreenViewModel = hiltViewModel()
){
    val posts = viewModel.posts
        .collectAsStateWithLifecycle()
        .value
        .map { it.toExternalModel() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text= "Your saved posts",
                    )
                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ){
            item{
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (posts.isEmpty()){
                        Text(
                            text = "You have no saved posts",
                            fontWeight = FontWeight.Bold,
                        )
                    }

                }
            }

            items(items = posts){ post ->
                ArticleCard(
                    post = post,
                    onItemClick = {
                        navigateToPostScreen(it._id)
                    },
                    onProfileNavigate = {

                    },
                    onDeletePost = {

                    },
                    profileImageUrl ="https://res.cloudinary.com/dhuqr5iyw/image/upload/v1640971757/mystory/profilepictures/default_y4mjwp.jpg",
                    isLiked = false,
                    isSaved =true,
                    isProfile = false
                )
            }
        }
    }


}