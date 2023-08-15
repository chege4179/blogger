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
package com.peterchege.blogger.presentation.screens.author_profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.domain.state.AuthorProfileFollowerFollowingUiState
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.FollowersList
import com.peterchege.blogger.presentation.components.FollowingList
import com.peterchege.blogger.presentation.components.LoadingComponent
import java.util.*

@Composable
fun AuthorFollowerFollowingScreen(

    navigateToAuthorProfileScreen:(String) -> Unit,
    viewModel: AuthorFollowerFollowingScreenViewModel = hiltViewModel()
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AuthorFollowerFollowingScreenContent(
        uiState = uiState,
        type = viewModel.type.value,
        navigateToAuthorProfileScreen = navigateToAuthorProfileScreen
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorFollowerFollowingScreenContent(
    uiState:AuthorProfileFollowerFollowingUiState,
    navigateToAuthorProfileScreen: (String) -> Unit,
    type:String,
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text= "User's " + type.toLowerCase(Locale.ROOT).capitalize(
                            Locale.ROOT) + "s",
                    )
                }
            )
        }
    ){ paddingValues ->
        when(uiState){
            is AuthorProfileFollowerFollowingUiState.Loading ->{
                LoadingComponent()
            }
            is AuthorProfileFollowerFollowingUiState.Error -> {
                ErrorComponent(
                    retryCallback = { /*TODO*/ },
                    errorMessage = uiState.message)
            }
            is AuthorProfileFollowerFollowingUiState.Success -> {
                when(type){
                    Constants.FOLLOWER -> {
                        FollowersList(
                            followers = uiState.data.followers,
                            navigateToAuthorProfileScreen = navigateToAuthorProfileScreen
                        )
                    }
                    Constants.FOLLOWING -> {
                        FollowingList(
                            following = uiState.data.following,
                            navigateToAuthorProfileScreen = navigateToAuthorProfileScreen,

                        )
                    }
                }
            }
        }

    }
}
