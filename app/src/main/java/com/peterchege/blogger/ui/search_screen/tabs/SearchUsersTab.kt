package com.peterchege.blogger.ui.search_screen.tabs

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.components.ProfileCard
import com.peterchege.blogger.ui.search_screen.SearchProductScreenViewModel
import com.peterchege.blogger.util.Screens

@OptIn(ExperimentalCoilApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchUsersTab(
    navHostController: NavHostController,

    viewModel: SearchProductScreenViewModel = hiltViewModel()
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)

    ) {
        if (viewModel.isLoading.value){
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        }else{
            if (viewModel.searchUser.value.isEmpty()){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "No users found")
                }
            }else{
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
                ) {
                    items(viewModel.searchUser.value) { user ->
                        ProfileCard(
                            navController = navHostController,
                            user =user,
                            onProfileNavigate = {
                                viewModel.onProfileNavigate(it,navHostController,navHostController)
                            }
                        )
                    }
                }
            }
        }



    }
}