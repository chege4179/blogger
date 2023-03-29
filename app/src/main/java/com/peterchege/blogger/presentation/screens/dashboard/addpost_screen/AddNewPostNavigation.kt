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
package com.peterchege.blogger.presentation.screens.dashboard.addpost_screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.presentation.screens.dashboard.draft_screen.DraftScreen

@Composable
fun AddPostScreenNavigation(
    bottomNavController: NavController

) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.ADD_NEW_POST_SCREEN) {
//        composable(Screens.ADD_NEW_POST_SCREEN){
//            AddPostScreen(navController,bottomNavController =bottomNavController)
//        }
        composable(Screens.DRAFT_SCREEN) {
            DraftScreen(navController)
        }
    }

}
