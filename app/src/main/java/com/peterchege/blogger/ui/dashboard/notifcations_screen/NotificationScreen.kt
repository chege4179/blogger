package com.peterchege.blogger.ui.dashboard.notifcations_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.peterchege.blogger.components.NotificationCard


@Composable
fun NotificationScreen(
    navController: NavController,
    navHostController: NavController,
    viewModel: NotificationViewModel = hiltViewModel()
){
    val notifications = viewModel.notifications.value
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
        ,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text= "Notifications",
                    )
                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }
    ) {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)){
            items(notifications.reversed()){ notification ->
                NotificationCard(
                    navController = navHostController,
                    notification =notification
                )
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
    }
}