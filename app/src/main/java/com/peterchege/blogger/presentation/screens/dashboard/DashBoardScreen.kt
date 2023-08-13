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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.presentation.components.BottomNavItem
import com.peterchege.blogger.presentation.navigation.navigateToAddPostScreen
import com.peterchege.blogger.presentation.navigation.navigateToAuthUserProfileScreen
import com.peterchege.blogger.presentation.navigation.navigateToAuthorProfileScreen
import com.peterchege.blogger.presentation.navigation.navigateToCategoryScreen
import com.peterchege.blogger.presentation.navigation.navigateToLoginScreen
import com.peterchege.blogger.presentation.navigation.navigateToPostScreen
import com.peterchege.blogger.presentation.navigation.navigateToProfileFollowerFollowingScreen
import com.peterchege.blogger.presentation.navigation.navigateToSearchScreen
import com.peterchege.blogger.presentation.screens.dashboard.feed_screen.FeedScreen
import com.peterchege.blogger.presentation.screens.dashboard.notifcations_screen.NotificationScreen
import com.peterchege.blogger.presentation.screens.dashboard.profile_screen.ProfileScreen
import com.peterchege.blogger.presentation.screens.dashboard.savedposts_screen.SavedPostScreen


@ExperimentalMaterialApi
@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.DarkGray,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color.Gray,
                onClick = { onItemClick(item) },
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name
                        )
                        if (selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp

                            )
                        }

                    }

                }
            )
        }


    }


}


@ExperimentalMaterialApi
@Composable
fun DashBoardScreen(
    navHostController: NavHostController,
    viewModel: DashBoardViewModel = hiltViewModel()

) {
    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavBar(
                items = listOf(
                    BottomNavItem(
                        name = "Home",
                        route = Screens.FEED_SCREEN,
                        icon = Icons.Default.Home
                    ),
                    BottomNavItem(
                        name = "Saved Posts",
                        route = Screens.SAVED_POST_SCREEN,
                        icon = Icons.Default.Favorite
                    ),

                    BottomNavItem(
                        name = "Notifications",
                        route = Screens.NOTIFICATION_SCREEN,
                        icon = Icons.Default.Notifications
                    ),
                    BottomNavItem(
                        name = "Profile",
                        route = Screens.PROFILE_SCREEN,
                        icon = Icons.Default.Person
                    )

                ),
                navController = bottomNavController,
                onItemClick = {
                    bottomNavController.navigate(it.route)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .padding(innerPadding)
        ) {
            DashboardNavigation(
                navHostController = navHostController,
                bottomNavController = bottomNavController
            )
        }

    }
}