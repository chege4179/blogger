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
    this.navigate(route = Screens.LOGIN_SCREEN)
}
fun NavController.navigateToSignUpScreen(){
    this.navigate(route = Screens.SIGNUP_SCREEN)
}

fun NavController.navigateToPostScreen(postId:String){
    this.navigate(route = Screens.POST_SCREEN + "/$postId")
}

fun NavController.navigateToDashBoard(){
    this.navigate(route = Screens.DASHBOARD_SCREEN)
}

fun NavController.navigateToDraftScreen(){
    this.navigate(route = Screens.DRAFT_SCREEN)
}

fun NavController.navigateToSearchScreen(){
    this.navigate(route = Screens.SEARCH_SCREEN)
}

fun NavController.navigateToCategoryScreen(category:String){
    this.navigate(route = Screens.CATEGORY_SCREEN +"/$category")
}
fun NavController.navigateToAuthorProfileScreen(username:String){
    this.navigate(route = Screens.AUTHOR_PROFILE_SCREEN +"/$username")
}

fun NavController.navigateToAuthorProfileFollowingScreen(username:String,type:String){
    this.navigate(route = Screens.AUTHOR_FOLLOWER_FOLLOWING_SCREEN +"/$username" +"/$type")
}

fun NavController.navigateToAddPostScreen(draftId:Int? = null){
    if (draftId == null){
        this.navigate(route = Screens.ADD_NEW_POST_SCREEN)
    }else{
        this.navigate(route = Screens.ADD_NEW_POST_SCREEN +"?draftId=${draftId}")
    }

}
fun NavController.navigateToProfileFollowerFollowingScreen(type:String){
    navigate(route = Screens.PROFILE_FOLLOWER_FOLLOWING_SCREEN + "/$type")
}
fun NavController.navigateToFeedScreen(){
    this.navigate(route = Screens.FEED_SCREEN)
}
fun NavController.navigateToSavedPostScreen(){
    this.navigate(route = Screens.SAVED_POST_SCREEN)
}
fun NavController.navigateToNotificationScreen(){
    this.navigate(route = Screens.NOTIFICATION_SCREEN)
}
fun NavController.navigateToAuthUserProfileScreen(){
    this.navigate(route = Screens.PROFILE_NAVIGATION)
}

