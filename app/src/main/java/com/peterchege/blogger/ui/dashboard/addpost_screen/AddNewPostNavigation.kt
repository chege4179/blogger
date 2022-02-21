package com.peterchege.blogger.ui.dashboard.addpost_screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peterchege.blogger.ui.dashboard.draft_screen.DraftScreen
import com.peterchege.blogger.util.Screens

@Composable
fun AddPostScreenNavigation(
    bottomNavController: NavController

){
    val navController = rememberNavController()
    NavHost(navController =navController, startDestination = Screens.ADD_NEW_POST_SCREEN ){
//        composable(Screens.ADD_NEW_POST_SCREEN){
//            AddPostScreen(navController,bottomNavController =bottomNavController)
//        }
        composable(Screens.DRAFT_SCREEN){
            DraftScreen(navController)
        }
    }

}
