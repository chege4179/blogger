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
import com.peterchege.blogger.presentation.screens.dashboard.feed_screen.FeedScreen
import com.peterchege.blogger.presentation.screens.dashboard.notifcations_screen.NotificationScreen
import com.peterchege.blogger.presentation.screens.dashboard.profile_screen.ProfileNavigation
import com.peterchege.blogger.presentation.screens.dashboard.savedposts_screen.SavedPostScreen


@Composable
fun DashboardNavigation(
    navController: NavHostController,
    navHostController: NavHostController
) {

    NavHost(

        navController = navController,
        startDestination = Screens.FEED_SCREEN
    ) {

        composable(
            route = Screens.FEED_SCREEN
        ) {
            FeedScreen(bottomNavController = navController, navHostController = navHostController)
        }
        composable(
            route = Screens.SAVED_POST_SCREEN
        ) {
            SavedPostScreen(
                bottomNavController = navController,
                navHostController = navHostController
            )
        }

        composable(
            route = Screens.NOTIFICATION_SCREEN
        ) {
            NotificationScreen(navController, navHostController = navHostController)
        }
        composable(
            route = Screens.PROFILE_NAVIGATION
        ) {
            ProfileNavigation(navController, navHostController = navHostController)
        }

    }

}