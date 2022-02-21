package com.peterchege.blogger.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.peterchege.blogger.api.responses.Follower
import com.peterchege.blogger.api.responses.Following
import com.peterchege.blogger.util.Screens

@Composable
fun FollowerCard(
    navController: NavController,
    follower:Follower,
    isFollowing:Boolean,
    removeFollower:(follower:Follower) -> Unit,
    followFollower:(follower:Follower) -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.LightGray)
            .clickable {
                       navController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/${follower.followerUsername}")
            }
        ,
        shape = RoundedCornerShape(10)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
//                .padding(5.dp)
                .background(Color.White)
            ,
            horizontalArrangement = Arrangement.SpaceAround,
        ){
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = follower.followerUsername)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = follower.followerFullname)

            }
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isFollowing){
                    Button(onClick = {
                        removeFollower(follower)

                    }) {
                        Text(text = "Un Follow")
                    }
                }else{
                    Button(onClick = {
                        followFollower(follower)
                    }) {
                        Text(text = "Follow")
                    }
                }
            }
        }
    }
}




@Composable
fun FollowingCard(
    navController: NavController,
    following:Following,
    unFollowUser:(following:Following) -> Unit,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.LightGray)
            .clickable {
                navController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/${following.followedUsername}")
            }
        ,
        shape = RoundedCornerShape(10)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
//                .padding(5.dp)
                .background(Color.White)
            ,
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = following.followedUsername)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = following.followedFullname)

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight()
            ) {
                Button(onClick = {
                    unFollowUser(following)

                }) {
                    Text(text = "Un Follow")
                }
            }
        }
    }



}