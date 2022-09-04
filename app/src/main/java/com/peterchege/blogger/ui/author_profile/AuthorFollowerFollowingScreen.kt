package com.peterchege.blogger.ui.author_profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.peterchege.blogger.components.FollowerCard
import com.peterchege.blogger.components.FollowingCard
import com.peterchege.blogger.util.Constants
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AuthorFollowerFollowingScreen(
    navController: NavController,
    viewModel: AuthorFollowerFollowingScreenViewModel = hiltViewModel()
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text= "User's " + viewModel.type.value.toLowerCase(Locale.ROOT).capitalize(
                            Locale.ROOT) + "s",
                    )
                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }
    ){

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ){
            if (viewModel.type.value == Constants.FOLLOWER){
                viewModel.user.value?.let { user ->
                    if (user.followers.isEmpty()){
                        item {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "This user not have any followers yet")

                            }
                        }
                    }else{
                        items(user.followers){ follower ->
                            FollowerCard(
                                navController =navController ,
                                follower =follower,
                                isFollowing = viewModel.getIsFollowingStatus(follower.followerUsername),
                                removeFollower = {

                                },
                                followFollower = {


                                },
                                isYourProfile = false
                            )
                        }
                    }

                }
            }else if (viewModel.type.value == Constants.FOLLOWING){
                viewModel.user.value?.let { user ->
                    if (user.following.isEmpty()){
                        item {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "This user isn't following anybody yet")

                            }
                        }
                    }else{
                        items(user.following){ following ->
                            FollowingCard(
                                navController =navController ,
                                following =following,
                                unFollowUser ={
                                },
                                isYourProfile = false
                            )
                        }
                    }
                }
            }
        }
    }
}