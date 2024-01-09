package com.peterchege.blogger.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FollowButtonSection(
    followUser:()-> Unit,
    unfollowUser:() -> Unit,
    isFollowingMe:Boolean,
    isAuthUserFollowingBack:Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        if (isAuthUserFollowingBack){
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    unfollowUser()
                }
            ) {
                Text(text = "Following")
            }
        }else{
            if (isFollowingMe){
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        followUser()
                    }
                ) {
                    Text(text = "Follow Back")
                }
            }else{
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        followUser()
                    }
                ) {
                    Text(text = "Follow")
                }
            }
        }
    }


}