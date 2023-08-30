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

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.presentation.navigation.navigateToAddPostScreen
import com.peterchege.blogger.presentation.navigation.navigateToAuthUserProfileScreen
import com.peterchege.blogger.presentation.navigation.navigateToAuthorProfileScreen
import com.peterchege.blogger.presentation.navigation.navigateToCategoryScreen
import com.peterchege.blogger.presentation.navigation.navigateToLoginScreen
import com.peterchege.blogger.presentation.navigation.navigateToPostScreen
import com.peterchege.blogger.presentation.navigation.navigateToProfileFollowerFollowingScreen
import com.peterchege.blogger.presentation.navigation.navigateToSearchScreen
import com.peterchege.blogger.presentation.navigation.navigateToSignUpScreen
import com.peterchege.blogger.presentation.screens.dashboard.feed_screen.FeedScreen
import com.peterchege.blogger.presentation.screens.dashboard.notifcations_screen.NotificationScreen
import com.peterchege.blogger.presentation.screens.dashboard.profile_screen.ProfileScreen
import com.peterchege.blogger.presentation.screens.dashboard.savedposts_screen.SavedPostScreen


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
            route = Screens.FEED_SCREEN
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
            route = Screens.SAVED_POST_SCREEN
        ) {
            SavedPostScreen(
                navigateToPostScreen = navHostController::navigateToPostScreen,
            )
        }

        composable(
            route = Screens.NOTIFICATION_SCREEN
        ) {
            NotificationScreen(
                navigateToPostScreen = navHostController::navigateToPostScreen,
                navigateToAuthorProfileScreen = navHostController::navigateToAuthorProfileScreen,
            )

        }
        composable(
            route = Screens.PROFILE_SCREEN
        ) {
            ProfileScreen(
                navigateToSignUpScreen = navHostController::navigateToSignUpScreen,
                navigateToLoginScreen = navHostController::navigateToLoginScreen,
                navigateToProfileFollowerFollowingScreen = navHostController::navigateToProfileFollowerFollowingScreen,
                navigateToPostScreen = navHostController::navigateToPostScreen,
            )
        }
    }

}