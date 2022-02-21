package com.peterchege.blogger.ui.dashboard.profile_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.peterchege.blogger.components.FollowerCard
import com.peterchege.blogger.components.FollowingCard
import com.peterchege.blogger.util.Constants


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
                        text= viewModel.type.value,
                    )
                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }
    ){

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ){

            if (viewModel.type.value == Constants.FOLLOWER){
                profileViewModel.state.value.user?.let { user ->
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


                        )
                    }
                }
            }else if (viewModel.type.value == Constants.FOLLOWING){
                profileViewModel.state.value.user?.let { user ->
                    items(user.following){ following ->
                        FollowingCard(
                            navController =navController ,
                            following =following,
                            unFollowUser ={
                                viewModel.unfollowUser(it.followedUsername)

                            },
                        )
                    }
                }
            }

        }



    }

}