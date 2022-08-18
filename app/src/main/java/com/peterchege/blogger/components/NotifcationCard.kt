package com.peterchege.blogger.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import com.peterchege.blogger.api.requests.Notification
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Screens

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
                if (notification.notificationType == "Like" || notification.notificationType =="Comment"){
                    navController.navigate(Screens.POST_SCREEN + "/${notification.postId}/${Constants.API_SOURCE}")

                }else{
                    navController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/${notification.notificationSender}")

                }

            },
        elevation = 3.dp

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(5.dp)
            ,
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

                    contentDescription = "")
            }
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
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