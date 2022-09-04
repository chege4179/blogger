package com.peterchege.blogger.ui.dashboard.profile_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.peterchege.blogger.components.FollowerCard
import com.peterchege.blogger.components.FollowingCard
import com.peterchege.blogger.util.Constants
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileFollowerFollowingScreen(
    navController: NavController,
    viewModel:ProfileFollowerFollowingScreenViewModel = hiltViewModel(),
    profileViewModel:ProfileViewModel = hiltViewModel(),
){

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text= "My " + viewModel.type.value.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT) ,
                    )
                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }
    ){
        if (viewModel.isLoading.value){
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                CircularProgressIndicator()
            }
        }else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ){
                if (viewModel.type.value == Constants.FOLLOWER){
                    profileViewModel.user.value?.let { user ->
                        if (user.followers.isEmpty()){
                            item {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "You do not have any followers yet")

                                }
                            }
                        }else{
                            items(user.followers){ follower ->
                                FollowerCard(
                                    navController =navController ,
                                    follower =follower,
                                    isFollowing = viewModel.getIsFollowingStatus(follower.followerUsername),
                                    removeFollower = {
                                        viewModel.unfollowUser(followedUsername = it.followerUsername)
                                    },
                                    followFollower = {
                                        viewModel.followUser(followedUsername = it.followerUsername)

                                    },
                                    isYourProfile = true


                                )
                            }
                        }

                    }
                }else if (viewModel.type.value == Constants.FOLLOWING){
                    profileViewModel.user.value?.let { user ->
                        if (user.following.isEmpty()){
                            item {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "You do not have any followers yet")

                                }
                            }
                        }else{
                            items(user.following){ following ->
                                FollowingCard(
                                    navController =navController ,
                                    following =following,
                                    unFollowUser ={
                                        viewModel.unfollowUser(it.followedUsername)

                                    },
                                    isYourProfile = true
                                )
                            }
                        }

                    }
                }
            }
        }


    }
}