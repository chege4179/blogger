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
package com.peterchege.blogger.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.peterchege.blogger.R
import com.peterchege.blogger.core.api.responses.Follower
import com.peterchege.blogger.core.api.responses.Following
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.core.util.generateAvatarURL

@Composable
fun FollowerCard(
    navigateToFollowerPage:(String) ->Unit,
    follower: Follower,
    isFollowing: Boolean,
    removeFollower: (follower: Follower) -> Unit,
    followFollower: (follower: Follower) -> Unit,
    isYourProfile: Boolean,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(70.dp)
            .clickable {
                navigateToFollowerPage(follower.followerUsername)

            },
        shape = RoundedCornerShape(15),
        elevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
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
                    .fillMaxWidth(if (isYourProfile) 0.5f else 0.9f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = follower.followerUsername,
                    fontWeight = FontWeight.Bold,

                    )
                Text(text = follower.followerFullname)

            }
            if (isYourProfile) {
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isFollowing) {
                        Button(onClick = {
                            removeFollower(follower)

                        }) {
                            Text(text = "Un Follow")
                        }
                    } else {
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
    following: Following,
    unFollowUser: (following: Following) -> Unit,
    isYourProfile: Boolean,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(70.dp)
            .clickable {
                navController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/${following.followedUsername}")
            },
        shape = RoundedCornerShape(15),
        elevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
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
                    .fillMaxWidth(if (isYourProfile) 0.5f else 0.9f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = following.followedUsername,
                    fontWeight = FontWeight.Bold,
                )
                Text(text = following.followedFullname)

            }
            if (isYourProfile) {
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