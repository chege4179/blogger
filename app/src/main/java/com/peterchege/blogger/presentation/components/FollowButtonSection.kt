/*
 * Copyright 2024 Blogger
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peterchege.blogger.R
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
                Text(text = stringResource(id = R.string.following_button_text))
            }
        }else{
            if (isFollowingMe){
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        followUser()
                    }
                ) {
                    Text(text = stringResource(id = R.string.follow_back_button_text))
                }
            }else{
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        followUser()
                    }
                ) {
                    Text(text = stringResource(id = R.string.follow_button_text))
                }
            }
        }
    }


}