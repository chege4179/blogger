package com.peterchege.blogger.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.peterchege.blogger.ui.author_profile.AuthorProfileNavigation
import com.peterchege.blogger.ui.author_profile.AuthorProfileScreen
import com.peterchege.blogger.ui.author_profile.FollowerFollowingScreen
import com.peterchege.blogger.ui.category_screen.CategoryScreen
import com.peterchege.blogger.ui.dashboard.DashBoardScreen
import com.peterchege.blogger.ui.dashboard.NavigationViewModel
import com.peterchege.blogger.ui.dashboard.addpost_screen.AddPostScreen
import com.peterchege.blogger.ui.dashboard.draft_screen.DraftScreen
import com.peterchege.blogger.ui.post_screen.PostScreen
import com.peterchege.blogger.ui.login.LoginScreen
import com.peterchege.blogger.ui.signup.SignUpScreen
import com.peterchege.blogger.util.Screens



@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: NavigationViewModel = hiltViewModel(),
) {
    val activity = (LocalContext.current as? Activity)
    NavHost(
        navController = navController,
        startDestination = viewModel.getInitialRoute()
    ){
        composable(Screens.LOGIN_SCREEN){
            LoginScreen(navController = navController)
            BackHandler(true) {
                // Or do nothing
                activity?.finish()

            }
        }
        composable(Screens.SIGNUP_SCREEN){
            SignUpScreen(navController = navController)
        }
        composable(Screens.POST_SCREEN + "/{postId}/{source}"){
            PostScreen(navController = navController)
        }
        composable(Screens.AUTHOR_PROFILE_NAVIGATION + "/{username}"){
            AuthorProfileNavigation()
        }
        composable(Screens.AUTHOR_PROFILE_SCREEN + "/{username}"){
            AuthorProfileScreen(navController = navController)
        }
        composable(Screens.FOLLOWER_FOLLOWING_SCREEN + "/{type}"){
            FollowerFollowingScreen(navController = navController)
        }
        composable(Screens.CATEGORY_SCREEN + "/{category}"){
            CategoryScreen(navController = navController)
        }
        composable(
            route = Screens.ADD_NEW_POST_SCREEN + "?postTitle={postTitle}&postBody={postBody}",
            arguments = listOf(
                navArgument("postTitle") {
                    defaultValue = ""
                },
                navArgument("postBody"){
                    defaultValue =""
                }
            )
        ){ backStackEntry ->
            AddPostScreen(
                navController = navController,
                bottomNavController = navController,
//                postBodyDraft = backStackEntry.arguments?.getString("postBody"),
//                postTitleDraft = backStackEntry.arguments?.getString("postTitle"),
            )

        }
        composable(
            route = Screens.DRAFT_SCREEN
        ){
            DraftScreen(navController = navController)
        }
        composable(
            route = Screens.DASHBOARD_SCREEN
        ){
            BackHandler(true) {
                // Or do nothing
                activity?.finish()

            }
            DashBoardScreen(navHostController = navController)

        }

    }

}