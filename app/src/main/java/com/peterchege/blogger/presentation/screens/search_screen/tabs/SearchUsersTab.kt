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
package com.peterchege.blogger.presentation.screens.search_screen.tabs

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.presentation.components.ProfileCard
import com.peterchege.blogger.presentation.screens.search_screen.SearchProductScreenViewModel

@OptIn(ExperimentalCoilApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchUsersTab(
    navHostController: NavHostController,

    viewModel: SearchProductScreenViewModel = hiltViewModel()
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)

    ) {
        if (viewModel.isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        } else {
            if (viewModel.searchUser.value.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "No users found")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
                ) {
                    items(viewModel.searchUser.value) { user ->
                        ProfileCard(
                            navController = navHostController,
                            user = user,
                            onProfileNavigate = {
                                viewModel.onProfileNavigate(
                                    it,
                                    navHostController,
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