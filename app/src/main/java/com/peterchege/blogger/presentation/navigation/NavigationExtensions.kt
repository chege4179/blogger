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
package com.peterchege.blogger.presentation.navigation

import androidx.navigation.NavController
import com.peterchege.blogger.core.util.Screens

fun NavController.navigateToLoginScreen(){
    navigate(route = Screens.LOGIN_SCREEN)
}
fun NavController.navigateToSignUpScreen(){
    navigate(route = Screens.SIGNUP_SCREEN)
}

fun NavController.navigateToPostScreen(postId:String){
    navigate(route = Screens.POST_SCREEN + "/$postId")
}

fun NavController.navigateToDashBoard(){
    navigate(route = Screens.DASHBOARD_SCREEN){

    }
}

fun NavController.navigateToDraftScreen(){
    navigate(route = Screens.DRAFT_SCREEN)
}

fun NavController.navigateToSearchScreen(){
    navigate(route = Screens.SEARCH_SCREEN)
}

fun NavController.navigateToCategoryScreen(category:String){
    navigate(route = Screens.CATEGORY_SCREEN +"/$category")
}
fun NavController.navigateToAuthorProfileScreen(username:String){
    navigate(route = Screens.AUTHOR_PROFILE_SCREEN +"/$username")
}

fun NavController.navigateToAuthorProfileFollowingScreen(username:String,type:String){
    navigate(route = Screens.AUTHOR_FOLLOWER_FOLLOWING_SCREEN +"/$username" +"/$type")
}

fun NavController.navigateToAddPostScreen(draftId:Int? = null){
    if (draftId == null){
        navigate(route = Screens.ADD_NEW_POST_SCREEN)
    }else{
        navigate(route = Screens.ADD_NEW_POST_SCREEN +"?draftId=${draftId}")
    }

}
fun NavController.navigateToProfileFollowerFollowingScreen(type:String){
    navigate(route = Screens.PROFILE_FOLLOWER_FOLLOWING_SCREEN + "/$type")
}

fun NavController.navigateToAuthUserProfileScreen(){
    navigate(route = Screens.PROFILE_NAVIGATION)
}

fun NavController.navigateToSettingsScreen(){
    navigate(route = Screens.SETTINGS_SCREEN)
}

fun NavController.navigateToAboutScreen(){
    navigate(route = Screens.ABOUT_SCREEN)
}

fun NavController.navigateToEditProfileScreen(){
    navigate(route = Screens.EDIT_PROFILE_SCREEN)
}

fun NavController.navigateToEditPostScreen(postId: String){
    navigate(route = Screens.EDIT_POST_SCREEN + "/$postId")
}
