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
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
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


@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoilApi::class, ExperimentalAnimationApi::class)
@ExperimentalMaterial3Api
@Composable
fun AppNavigation(
    navController: NavHostController,
    startOSSActivity:() -> Unit,
) {
    val activity = (LocalContext.current as? Activity)

    NavHost(
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        },
        navController = navController,
        startDestination = Screens.DASHBOARD_SCREEN
    ) {
        composable(
            route = Screens.LOGIN_SCREEN,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
            LoginScreen(
                navigateToDashBoard = navController::navigateToDashBoard,
                navigateToSignUpScreen = navController::navigateToSignUpScreen,
            )
            BackHandler(enabled = true) {
                activity?.finish()
            }
        }
        composable(
            route = Screens.SIGNUP_SCREEN,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
            SignUpScreen(
                navigateToLoginScreen = navController::navigateToLoginScreen,
            )
        }
        composable(
            route = Screens.SEARCH_SCREEN,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
            SearchScreen(
                navigateToPostScreen = navController::navigateToPostScreen,
                navigateToAuthorProfileScreen = navController::navigateToAuthorProfileScreen,
            )
        }
        composable(
            route = Screens.POST_SCREEN + "/{postId}",
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
            PostScreen()
        }
        composable(
            route = Screens.AUTHOR_PROFILE_SCREEN + "/{userId}",
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
            AuthorProfileScreen(
                navigateToPostScreen = navController::navigateToPostScreen,
                navigateToAuthorFollowerFollowingScreen = navController::navigateToAuthorProfileFollowingScreen
            )
        }
        composable(
            route = Screens.AUTHOR_FOLLOWER_FOLLOWING_SCREEN + "/{userId}" + "/{type}",
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
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
        composable(
            route = Screens.CATEGORY_SCREEN + "/{category}",
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
            CategoryScreen()
        }
        composable(
            route = Screens.PROFILE_FOLLOWER_FOLLOWING_SCREEN + "/{type}",
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ){
            val type = it.arguments?.getString("type")
                ?: throw IllegalStateException("Type data missing.")
            ProfileFollowerFollowingScreen(
                navigateToAuthorProfileScreen = navController::navigateToAuthorProfileScreen,
                type = type
            )
        }

        composable(
            route = Screens.ADD_NEW_POST_SCREEN + "?draftId={draftId}",
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
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
            route = Screens.DRAFT_SCREEN,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
            DraftScreen(
                navigateToAddPostScreen = navController::navigateToAddPostScreen,
                navigateBack = navController::popBackStack
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
            route = Screens.SETTINGS_SCREEN,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
            SettingsScreen(
                navigateHome = navController::navigateToDashBoard,
                startOSSActivity = startOSSActivity,
            )

        }
        composable(
            route = Screens.ABOUT_SCREEN,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
            AboutScreen()
        }
        composable(
            route = Screens.EDIT_PROFILE_SCREEN,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ){
            EditProfileScreen()
        }

        composable(
            route = Screens.EDIT_POST_SCREEN,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ){
            val post = navController.previousBackStackEntry?.savedStateHandle?.get<Post>(key ="post")
            EditPostScreen(post = post)
        }
    }

}