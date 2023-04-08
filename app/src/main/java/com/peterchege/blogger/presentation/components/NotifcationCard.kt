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
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.peterchege.blogger.core.api.requests.Notification
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.Screens

@Composable
fun NotificationCard(
    navController: NavController,
    notification: Notification
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                if (notification.notificationType == "Like" || notification.notificationType == "Comment") {
                    navController.navigate(Screens.POST_SCREEN + "/${notification.postId}/${Constants.API_SOURCE}")

                } else {
                    navController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/${notification.notificationSender}")

                }

            },
        elevation = 3.dp

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            notification.notificationImageUrl?.let {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .height(60.dp),
                    painter = rememberImagePainter(
                        data = notification.notificationImageUrl,
                        builder = {
                            crossfade(true)

                        },
                    ),

                    contentDescription = ""
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center

            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = notification.notificationContent,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,

                    )
            }


        }

    }

}