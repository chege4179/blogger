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
package com.peterchege.blogger.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.peterchege.blogger.ui.author_profile.AuthorFollowerFollowingScreen
import com.peterchege.blogger.ui.author_profile.AuthorProfileNavigation
import com.peterchege.blogger.ui.author_profile.AuthorProfileScreen

import com.peterchege.blogger.ui.category_screen.CategoryScreen
import com.peterchege.blogger.ui.dashboard.DashBoardScreen
import com.peterchege.blogger.ui.dashboard.NavigationViewModel
import com.peterchege.blogger.ui.dashboard.addpost_screen.AddPostScreen
import com.peterchege.blogger.ui.dashboard.draft_screen.DraftScreen

import com.peterchege.blogger.ui.post_screen.PostScreen
import com.peterchege.blogger.ui.login.LoginScreen
import com.peterchege.blogger.ui.search_screen.SearchScreen
import com.peterchege.blogger.ui.signup.SignUpScreen
import com.peterchege.blogger.util.Screens



@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: NavigationViewModel = hiltViewModel(),
) {
    val activity = (LocalContext.current as? Activity)
    NavHost(
        navController = navController,
        startDestination = viewModel.getInitialRoute()
    ){
        composable(Screens.LOGIN_SCREEN){
            LoginScreen(navController = navController)
            BackHandler(true) {
                // Or do nothing
                activity?.finish()

            }
        }
        composable(Screens.SIGNUP_SCREEN){
            SignUpScreen(navController = navController)
        }
        composable(Screens.SEARCH_SCREEN){
            SearchScreen(navController = navController)
        }
        composable(Screens.POST_SCREEN + "/{postId}/{source}"){
            PostScreen(navController = navController)
        }
        composable(Screens.AUTHOR_PROFILE_NAVIGATION + "/{username}"){
            AuthorProfileNavigation()
        }
        composable(Screens.AUTHOR_PROFILE_SCREEN + "/{username}"){
            AuthorProfileScreen(navController = navController)
        }
        composable(Screens.AUTHOR_FOLLOWER_FOLLOWING_SCREEN + "/{username}" + "/{type}"){
            AuthorFollowerFollowingScreen(navController = navController)
        }
        composable(Screens.CATEGORY_SCREEN + "/{category}"){
            CategoryScreen(navController = navController)
        }


        composable(
            route = Screens.ADD_NEW_POST_SCREEN + "?postTitle={postTitle}&postBody={postBody}",
            arguments = listOf(
                navArgument("postTitle") {
                    defaultValue = ""
                },
                navArgument("postBody"){
                    defaultValue =""
                }
            )
        ){ backStackEntry ->
            AddPostScreen(
                navController = navController,
                bottomNavController = navController,
//                postBodyDraft = backStackEntry.arguments?.getString("postBody"),
//                postTitleDraft = backStackEntry.arguments?.getString("postTitle"),
            )

        }
        composable(
            route = Screens.DRAFT_SCREEN
        ){
            DraftScreen(navController = navController)
        }
        composable(
            route = Screens.DASHBOARD_SCREEN
        ){
            BackHandler(true) {
                // Or do nothing
                activity?.finish()

            }
            DashBoardScreen(navHostController = navController)

        }

    }

}