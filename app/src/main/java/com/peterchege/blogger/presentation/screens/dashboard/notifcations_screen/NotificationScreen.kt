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
package com.peterchege.blogger.presentation.screens.dashboard.notifcations_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.peterchege.blogger.presentation.components.NotificationCard

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
                        text= "My Notifications",
                    )
                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }
    ) {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)){
            if (notifications.isEmpty()){
                item{
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ){
                        Text(text = "You have no notifications yet")
                    }
                }
            }else{
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
}