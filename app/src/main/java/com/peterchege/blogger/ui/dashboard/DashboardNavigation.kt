package com.peterchege.blogger.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.peterchege.blogger.ui.dashboard.addpost_screen.AddPostScreen
import com.peterchege.blogger.ui.dashboard.addpost_screen.AddPostScreenNavigation
import com.peterchege.blogger.ui.dashboard.home_screen.HomeScreen
import com.peterchege.blogger.ui.dashboard.notifcations_screen.NotificationScreen
import com.peterchege.blogger.ui.dashboard.profile_screen.ProfileNavigation
import com.peterchege.blogger.ui.dashboard.profile_screen.ProfileScreen
import com.peterchege.blogger.ui.dashboard.savedposts_screen.SavedPostScreen
import com.peterchege.blogger.util.Screens
import java.lang.reflect.Modifier


@Composable
fun DashboardNavigation(
    navController: NavHostController,
    navHostController: NavHostController
) {

    NavHost(

        navController = navController,
        startDestination = Screens.HOME_SCREEN ){

        composable(
            route = Screens.HOME_SCREEN
        ){
            HomeScreen(bottomNavController = navController, navHostController = navHostController)
        }
        composable(
            route = Screens.SAVED_POST_SCREEN
        ){
            SavedPostScreen(bottomNavController = navController, navHostController = navHostController)
        }

        composable(
            route = Screens.NOTIFICATION_SCREEN
        ){
            NotificationScreen(navController,navHostController=  navHostController)
        }
        composable(
            route = Screens.PROFILE_NAVIGATION
        ){
            ProfileNavigation(navController, navHostController = navHostController)
        }

    }

}