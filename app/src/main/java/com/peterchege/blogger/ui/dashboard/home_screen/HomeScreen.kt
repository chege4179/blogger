package com.peterchege.blogger.ui.dashboard.home_screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.ui.dashboard.home_screen.feed_screen.FeedScreen
import com.peterchege.blogger.ui.post_screen.PostScreen
import com.peterchege.blogger.util.Screens
import java.lang.reflect.Modifier

@ExperimentalCoilApi
@Composable
fun HomeScreen(
    bottomNavController: NavController,
    navHostController: NavHostController
){
    FeedScreen(bottomNavController = bottomNavController,navHostController= navHostController)
}