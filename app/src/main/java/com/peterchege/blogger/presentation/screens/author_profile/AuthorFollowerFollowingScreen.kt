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

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.peterchege.blogger.core.api.responses.Follower
import com.peterchege.blogger.core.api.responses.Following
import com.peterchege.blogger.presentation.components.FollowerCard
import com.peterchege.blogger.presentation.components.FollowingCard
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.domain.state.AuthorProfileFollowerFollowingUi
import com.peterchege.blogger.domain.state.AuthorProfileFollowerFollowingUiState
import com.peterchege.blogger.domain.state.AuthorProfileScreenUiState
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


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }
    ){
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
