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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.peterchege.blogger.core.api.responses.models.Notification
import com.peterchege.blogger.core.api.responses.models.PostAuthor

@Composable
fun NotificationCard(
    navigateToPostScreen: (String) -> Unit,
    navigateToAuthorProfileScreen: (String) -> Unit,
    notification: Notification
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(5.dp)
            .clickable {
                if (notification.notificationType == "Like" || notification.notificationType == "Comment") {
//                    notification.n?.let { navigateToPostScreen(it) }
                } else {
                    navigateToAuthorProfileScreen(notification.notificationSender.userId)
                }

            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileAvatar(
            imageUrl = notification.notificationSender.imageUrl,
            modifier = Modifier.padding(horizontal = 5.dp),
            size = 48
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = notification.notificationContent,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}


@Preview
@Composable
fun NotificationCardPreview() {
    NotificationCard(
        navigateToPostScreen = {},
        navigateToAuthorProfileScreen = {},
        notification =
        Notification(
            notificationId = "",
            notificationContent = "Peter Chege has liked your comment",
            notificationPostId = "",
            notificationType = "Like",
            createdAt = "",
            updatedAt = "",
            senderId = "",
            recieverId = "",
            notificationCommentId = "",
            notificationSender = PostAuthor(
                userId = "",
                email = "",
                username = "",
                password = "",
                createdAt = "2023-12-02T18:55:36.935Z",
                updatedAt = "2023-12-02T18:55:36.935Z",
                imageUrl = "",
                fullName = "Peter Chege"
            ),
            notificationReceiver = PostAuthor(
                userId = "",
                email = "",
                username = "",
                password = "",
                createdAt = "2023-12-02T18:55:36.935Z",
                updatedAt = "2023-12-02T18:55:36.935Z",
                imageUrl = "",
                fullName = "Peter Chege"
            ),
        )
    )
}