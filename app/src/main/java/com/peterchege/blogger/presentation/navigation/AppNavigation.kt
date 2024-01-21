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
package com.peterchege.blogger.presentation.navigation

import android.app.Activity
import android.view.Window
import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.presentation.screens.about.AboutScreen
import com.peterchege.blogger.presentation.screens.author.AuthorFollowerFollowingScreen
import com.peterchege.blogger.presentation.screens.author.AuthorProfileScreen
import com.peterchege.blogger.presentation.screens.category.CategoryScreen
import com.peterchege.blogger.presentation.screens.dashboard.DashBoardScreen
import com.peterchege.blogger.presentation.screens.add_post.AddPostScreen
import com.peterchege.blogger.presentation.screens.draft.DraftScreen
import com.peterchege.blogger.presentation.screens.edit_post.EditPostScreen
import com.peterchege.blogger.presentation.screens.edit_profile.EditProfileScreen
import com.peterchege.blogger.presentation.screens.profile.ProfileFollowerFollowingScreen
import com.peterchege.blogger.presentation.screens.login.LoginScreen
import com.peterchege.blogger.presentation.screens.post.PostScreen
import com.peterchege.blogger.presentation.screens.search.SearchScreen
import com.peterchege.blogger.presentation.screens.settings.SettingsScreen
import com.peterchege.blogger.presentation.screens.signup.SignUpScreen


@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoilApi::class)
@ExperimentalMaterial3Api
@Composable
fun AppNavigation(
    navController: NavHostController,
    startOSSActivity:() -> Unit,
) {
    val activity = (LocalContext.current as? Activity)


    NavHost(
        navController = navController,
        startDestination = Screens.DASHBOARD_SCREEN
    ) {
        composable(route = Screens.LOGIN_SCREEN) {
            LoginScreen(
                navigateToDashBoard = navController::navigateToDashBoard,
                navigateToSignUpScreen = navController::navigateToSignUpScreen,
            )
            BackHandler(enabled = true) {
                activity?.finish()
            }
        }
        composable(route = Screens.SIGNUP_SCREEN) {
            SignUpScreen(
                navigateToLoginScreen = navController::navigateToLoginScreen,
            )
        }
        composable(route = Screens.SEARCH_SCREEN) {
            SearchScreen(
                navigateToPostScreen = navController::navigateToPostScreen,
                navigateToAuthorProfileScreen = navController::navigateToAuthorProfileScreen,
            )
        }
        composable(route = Screens.POST_SCREEN + "/{postId}") {
            PostScreen()
        }
        composable(route = Screens.AUTHOR_PROFILE_SCREEN + "/{userId}") {
            AuthorProfileScreen(
                navigateToPostScreen = navController::navigateToPostScreen,
                navigateToAuthorFollowerFollowingScreen = navController::navigateToAuthorProfileFollowingScreen
            )
        }
        composable(route = Screens.AUTHOR_FOLLOWER_FOLLOWING_SCREEN + "/{userId}" + "/{type}") {
            val type = it.arguments?.getString("type")
                ?: throw IllegalStateException("Type data missing.")
            val userId = it.arguments?.getString("type")
                ?: throw IllegalStateException("User ID data missing.")
            AuthorFollowerFollowingScreen(
                navigateToAuthorProfileScreen = navController::navigateToAuthorProfileScreen,
                type = type,
                userId = userId,
            )
        }
        composable(route = Screens.CATEGORY_SCREEN + "/{category}") {
            CategoryScreen()
        }
        composable(route = Screens.PROFILE_FOLLOWER_FOLLOWING_SCREEN + "/{type}"){
            val type = it.arguments?.getString("type")
                ?: throw IllegalStateException("Type data missing.")
            ProfileFollowerFollowingScreen(
                navigateToAuthorProfileScreen = navController::navigateToAuthorProfileScreen,
                type = type
            )
        }

        composable(
            route = Screens.ADD_NEW_POST_SCREEN + "?draftId={draftId}",
            arguments = listOf(
                navArgument(name = "draftId") {
                    type = NavType.IntType
                    defaultValue = 10000
                },

            )
        ) { backStackEntry ->
            AddPostScreen(
                navigateBack = navController::navigateToDashBoard,
                navigateToDraftScreen = navController::navigateToDraftScreen,
                navigateToDashboardScreen = navController::navigateToDashBoard,
                navigateToLoginScreen = navController::navigateToLoginScreen,
                navigateToSignUpScreen = navController::navigateToSignUpScreen,
            )

        }
        composable(
            route = Screens.DRAFT_SCREEN
        ) {
            DraftScreen(
                navigateToAddPostScreen = navController::navigateToAddPostScreen,
            )
        }

        composable(
            route = Screens.DASHBOARD_SCREEN
        ) {
            BackHandler(enabled = true) {
                activity?.finish()
            }
            DashBoardScreen(navHostController = navController)

        }
        composable(
            route = Screens.SETTINGS_SCREEN
        ) {
            SettingsScreen(
                navigateHome = navController::navigateToDashBoard,
                startOSSActivity = startOSSActivity,
            )

        }
        composable(
            route = Screens.ABOUT_SCREEN
        ) {
            AboutScreen()
        }
        composable(
            route = Screens.EDIT_PROFILE_SCREEN
        ){
            EditProfileScreen()
        }

        composable(
            route = Screens.EDIT_POST_SCREEN,
        ){
            val post = navController.previousBackStackEntry?.savedStateHandle?.get<Post>(key ="post")
            EditPostScreen(post = post)
        }
    }

}