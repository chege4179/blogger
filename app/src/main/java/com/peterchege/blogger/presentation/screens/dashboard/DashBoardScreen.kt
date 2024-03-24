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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.peterchege.blogger.R
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.core.util.findActivity
import com.peterchege.blogger.presentation.components.BottomNavItem


@ExperimentalMaterial3Api
@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit,
    shouldShowNavRail:Boolean,
    shouldShowBottomBar:Boolean
) {

    val backStackEntry = navController.currentBackStackEntryAsState()
    if (shouldShowNavRail){
        NavigationRail{
            Spacer(modifier = Modifier.weight(1f))
            items.forEachIndexed { index, item ->
                val selected = item.route == backStackEntry.value?.destination?.route
                NavigationRailItem(
                    icon = {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.name
                        )
                    },
                    label = { Text(text = item.name) },

                    selected = selected,
                    onClick = { onItemClick(item) }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
    if (shouldShowBottomBar){
        NavigationBar {
            items.forEachIndexed { index, item ->
                val selected = item.route == backStackEntry.value?.destination?.route
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.name
                        )
                    },
                    label = { Text(text = item.name) },

                    selected = selected,
                    onClick = { onItemClick(item) }
                )
            }
        }
    }



}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@ExperimentalMaterial3Api
@Composable
fun DashBoardScreen(
    navHostController: NavHostController,
) {
    val bottomNavController = rememberNavController()
    val bottomBarItems = listOf(
        BottomNavItem(
            name = stringResource(id = R.string.home_navbar),
            route = Screens.FEED_SCREEN,
            selectedIcon = Icons.Default.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        BottomNavItem(
            name = stringResource(id = R.string.saved_navbar),
            route = Screens.SAVED_POST_SCREEN,
            selectedIcon = Icons.Default.Favorite,
            unselectedIcon = Icons.Default.FavoriteBorder
        ),

        BottomNavItem(
            name = stringResource(id = R.string.notifications_navbar),
            route = Screens.NOTIFICATION_SCREEN,
            selectedIcon = Icons.Default.Notifications,
            unselectedIcon = Icons.Default.NotificationsNone
        ),
        BottomNavItem(
            name = stringResource(id = R.string.profile_navbar),
            route = Screens.PROFILE_SCREEN,
            selectedIcon = Icons.Default.Person,
            unselectedIcon = Icons.Outlined.Person
        )

    )
    val context = LocalContext.current
    val windowSizeClass = calculateWindowSizeClass(activity = context.findActivity())
    val shouldShowBottomBar: Boolean = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
    val shouldShowNavRail: Boolean = !shouldShowBottomBar
    Scaffold(
        bottomBar = {
            BottomNavBar(
                items = bottomBarItems,
                navController = bottomNavController,
                shouldShowBottomBar = shouldShowBottomBar,
                shouldShowNavRail = shouldShowNavRail,
                onItemClick = {
                    bottomNavController.navigate(it.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(bottomNavController.graph.findStartDestination().id) {
                            saveState = true

                        }
                    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DashboardPreview() {
    BottomNavBar(
        items = listOf(
            BottomNavItem(
                name = stringResource(id = R.string.home_navbar),
                route = Screens.FEED_SCREEN,
                selectedIcon = Icons.Default.Home,
                unselectedIcon = Icons.Outlined.Home,
            ),
            BottomNavItem(
                name = stringResource(id = R.string.saved_navbar),
                route = Screens.SAVED_POST_SCREEN,
                selectedIcon = Icons.Default.Favorite,
                unselectedIcon = Icons.Default.FavoriteBorder
            ),

            BottomNavItem(
                name = stringResource(id = R.string.notifications_navbar),
                route = Screens.NOTIFICATION_SCREEN,
                selectedIcon = Icons.Default.Notifications,
                unselectedIcon = Icons.Default.NotificationsNone
            ),
            BottomNavItem(
                name = stringResource(id = R.string.profile_navbar),
                route = Screens.PROFILE_SCREEN,
                selectedIcon = Icons.Default.Person,
                unselectedIcon = Icons.Outlined.Person
            )

        ),
        navController = rememberNavController(),
        shouldShowBottomBar = true,
        shouldShowNavRail = false,
        onItemClick = {

        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DashboardPreview2() {
    BottomNavBar(
        items = listOf(
            BottomNavItem(
                name = stringResource(id = R.string.home_navbar),
                route = Screens.FEED_SCREEN,
                selectedIcon = Icons.Default.Home,
                unselectedIcon = Icons.Outlined.Home,
            ),
            BottomNavItem(
                name = stringResource(id = R.string.saved_navbar),
                route = Screens.SAVED_POST_SCREEN,
                selectedIcon = Icons.Default.Favorite,
                unselectedIcon = Icons.Default.FavoriteBorder
            ),

            BottomNavItem(
                name = stringResource(id = R.string.notifications_navbar),
                route = Screens.NOTIFICATION_SCREEN,
                selectedIcon = Icons.Default.Notifications,
                unselectedIcon = Icons.Default.NotificationsNone
            ),
            BottomNavItem(
                name = stringResource(id = R.string.profile_navbar),
                route = Screens.PROFILE_SCREEN,
                selectedIcon = Icons.Default.Person,
                unselectedIcon = Icons.Outlined.Person
            )

        ),
        navController = rememberNavController(),
        shouldShowBottomBar = false,
        shouldShowNavRail = true,
        onItemClick = {

        }
    )

}
