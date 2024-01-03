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

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.components.ProfileCard
import com.peterchege.blogger.presentation.screens.search.SearchScreenUiState

@OptIn(ExperimentalCoilApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchUsersTab(
    navigateToAuthorProfileScreen:(String) -> Unit,
    uiState: SearchScreenUiState,
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)

    ) { paddingValues ->
        when (uiState) {
            is SearchScreenUiState.Idle -> {

            }

            is SearchScreenUiState.Searching -> {
                LoadingComponent()
            }

            is SearchScreenUiState.Error -> {
                ErrorComponent(
                    retryCallback = { /*TODO*/ },
                    errorMessage = uiState.message
                )
            }

            is SearchScreenUiState.ResultsFound -> {
                val searchUsers = uiState.users
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(items = searchUsers) { user ->
                        ProfileCard(
                            navigateToAuthorProfileScreen = navigateToAuthorProfileScreen,
                            user = user,

                        )
                    }
                }
            }
        }
    }
}