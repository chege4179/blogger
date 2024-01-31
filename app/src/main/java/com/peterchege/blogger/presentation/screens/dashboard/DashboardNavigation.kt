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
package com.peterchege.blogger.presentation.screens.dashboard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.presentation.navigation.navigateToAddPostScreen
import com.peterchege.blogger.presentation.navigation.navigateToAuthUserProfileScreen
import com.peterchege.blogger.presentation.navigation.navigateToAuthorProfileScreen
import com.peterchege.blogger.presentation.navigation.navigateToCategoryScreen
import com.peterchege.blogger.presentation.navigation.navigateToEditPostScreen
import com.peterchege.blogger.presentation.navigation.navigateToEditProfileScreen
import com.peterchege.blogger.presentation.navigation.navigateToLoginScreen
import com.peterchege.blogger.presentation.navigation.navigateToPostScreen
import com.peterchege.blogger.presentation.navigation.navigateToProfileFollowerFollowingScreen
import com.peterchege.blogger.presentation.navigation.navigateToSearchScreen
import com.peterchege.blogger.presentation.navigation.navigateToSettingsScreen
import com.peterchege.blogger.presentation.navigation.navigateToSignUpScreen
import com.peterchege.blogger.presentation.navigation.scaleInEnterTransition
import com.peterchege.blogger.presentation.navigation.scaleInPopEnterTransition
import com.peterchege.blogger.presentation.navigation.scaleOutExitTransition
import com.peterchege.blogger.presentation.navigation.scaleOutPopExitTransition
import com.peterchege.blogger.presentation.screens.feed.FeedScreen
import com.peterchege.blogger.presentation.screens.notifcations.NotificationScreen
import com.peterchege.blogger.presentation.screens.profile.ProfileScreen
import com.peterchege.blogger.presentation.screens.saved_posts.SavedPostScreen


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DashboardNavigation(
    navHostController: NavHostController,
    bottomNavController:NavHostController
) {

    NavHost(
        navController = bottomNavController,
        startDestination = Screens.FEED_SCREEN
    ){
        composable(
            route = Screens.FEED_SCREEN,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
            FeedScreen(
                navigateToAddPostScreen = navHostController::navigateToAddPostScreen,
                navigateToAuthorProfileScreen = navHostController::navigateToAuthorProfileScreen,
                navigateToCategoryScreen = navHostController::navigateToCategoryScreen,
                navigateToPostScreen = navHostController::navigateToPostScreen,
                navigateToSearchScreen = navHostController::navigateToSearchScreen,
                navigateToAuthUserProfileScreen = navHostController::navigateToAuthUserProfileScreen,
            )
        }
        composable(
            route = Screens.SAVED_POST_SCREEN,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
            SavedPostScreen(
                navigateToPostScreen = navHostController::navigateToPostScreen,
                navigateToAuthorProfileScreen = navHostController::navigateToAuthorProfileScreen
            )
        }

        composable(
            route = Screens.NOTIFICATION_SCREEN,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
            NotificationScreen(
                navigateToPostScreen = navHostController::navigateToPostScreen,
                navigateToAuthorProfileScreen = navHostController::navigateToAuthorProfileScreen,
            )

        }
        composable(
            route = Screens.PROFILE_SCREEN,
            enterTransition = { scaleInEnterTransition() },
            exitTransition = { scaleOutExitTransition() },
            popEnterTransition = { scaleInPopEnterTransition() },
            popExitTransition = { scaleOutPopExitTransition() },
        ) {
            ProfileScreen(
                navigateToSignUpScreen = navHostController::navigateToSignUpScreen,
                navigateToLoginScreen = navHostController::navigateToLoginScreen,
                navigateToProfileFollowerFollowingScreen = navHostController::navigateToProfileFollowerFollowingScreen,
                navigateToPostScreen = navHostController::navigateToPostScreen,
                navigateToEditProfileScreen = navHostController::navigateToEditProfileScreen,
                navigateToSettingsScreen = navHostController::navigateToSettingsScreen,
                navigateToEditPostScreen = navHostController::navigateToEditPostScreen
            )
        }
    }

}