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
package com.peterchege.blogger.ui.dashboard.profile_screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peterchege.blogger.ui.author_profile.AuthorFollowerFollowingScreen
import com.peterchege.blogger.ui.author_profile.AuthorProfileScreen
import com.peterchege.blogger.util.Screens

@Composable
fun ProfileNavigation(
    bottomNavController: NavController,
    navHostController: NavHostController,

){
    val navController  = rememberNavController()
    NavHost(navController =navController, startDestination = Screens.PROFILE_SCREEN ){
        composable(Screens.PROFILE_SCREEN){
            ProfileScreen(navController = navController,navHostController = navHostController)
        }
        composable(Screens.PROFILE_FOLLOWER_FOLLOWING_SCREEN + "/{type}"){
            ProfileFollowerFollowingScreen(navController = navController)
        }
        composable(Screens.AUTHOR_PROFILE_SCREEN + "/{username}"){
            AuthorProfileScreen(navController = navController)
        }
        composable(Screens.AUTHOR_FOLLOWER_FOLLOWING_SCREEN + "/{username}" + "/{type}"){
            AuthorFollowerFollowingScreen(navController = navController)
        }

    }
}