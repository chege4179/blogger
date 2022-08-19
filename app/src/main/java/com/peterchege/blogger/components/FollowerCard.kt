package com.peterchege.blogger.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.peterchege.blogger.R
import com.peterchege.blogger.api.responses.Follower
import com.peterchege.blogger.api.responses.Following
import com.peterchege.blogger.util.Screens
import com.peterchege.blogger.util.generateAvatarURL

@Composable
fun FollowerCard(
    navController: NavController,
    follower:Follower,
    isFollowing:Boolean,
    removeFollower:(follower:Follower) -> Unit,
    followFollower:(follower:Follower) -> Unit,
    isYourProfile:Boolean,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(70.dp)
            .clickable {
                navController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/${follower.followerUsername}")
            }
        ,
        shape = RoundedCornerShape(15),
        elevation = 3.dp
    ){
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Spacer(modifier = Modifier.width(10.dp))
            SubcomposeAsyncImage(
                model = generateAvatarURL(follower.followerFullname),
                loading = {
                    Image(
                        painter = painterResource(id = R.mipmap.default_profile),
                        contentDescription = "Default Profile Picture",
                        modifier = Modifier
                            .width(48.dp)
                            .height(48.dp)
                    )
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                contentDescription = "Profile Photo URL"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(if (isYourProfile)  0.5f else 0.9f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = follower.followerUsername,
                    fontWeight = FontWeight.Bold,

                    )
                Text(text=follower.followerFullname)

            }
            if (isYourProfile){
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
}

@Composable
fun FollowingCard(
    navController: NavController,
    following:Following,
    unFollowUser:(following:Following) -> Unit,
    isYourProfile:Boolean,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(70.dp)
            .clickable {
                navController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/${following.followedUsername}")
            }
        ,
        shape = RoundedCornerShape(15),
        elevation = 3.dp
    ){
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Spacer(modifier = Modifier.width(10.dp))
            SubcomposeAsyncImage(
                model = generateAvatarURL(following.followedFullname),
                loading = {
                    Image(
                        painter = painterResource(id = R.mipmap.default_profile),
                        contentDescription = "Default Profile Picture",
                        modifier = Modifier
                            .width(48.dp)
                            .height(48.dp)
                    )
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                contentDescription = "Profile Photo URL"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(if (isYourProfile)  0.5f else 0.9f )
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = following.followedUsername,
                    fontWeight = FontWeight.Bold,)
                Text(text=following.followedFullname)

            }
            if (isYourProfile){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Button(

                        onClick = {
                            unFollowUser(following)
                        }) {
                        Text(text = "Un Follow")
                    }

                }
            }


        }

    }



}