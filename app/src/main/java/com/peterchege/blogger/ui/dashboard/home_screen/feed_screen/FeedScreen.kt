package com.peterchege.blogger.ui.dashboard.home_screen.feed_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.api.responses.Post
import com.peterchege.blogger.components.ArticleCard
import com.peterchege.blogger.components.ProfileCard
import com.peterchege.blogger.ui.dashboard.home_screen.feed_screen.FeedViewModel
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Screens

@Composable
@ExperimentalCoilApi
fun FeedScreen(
    bottomNavController:NavController,
    navHostController: NavHostController,
    viewModel: FeedViewModel = hiltViewModel()
){
    val state = viewModel.state.value
    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(Screens.ADD_NEW_POST_SCREEN)
                }
            ) {
                Icon(Icons.Filled.Add,"")
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            if (state.isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
                ,
            ) {

                TextField(
                    value = viewModel.searchTerm.value,
                    onValueChange ={
                        viewModel.onChangeSearchTerm(it)
                    },
                    label ={
                        Text(text="Search posts or users")
                    },
                    modifier = Modifier.fillMaxWidth()

                )
                if (!viewModel.isFound.value){
                    Box(
                        modifier = Modifier.fillMaxSize(),

                    ){
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "No posts or users were found ... Please try a different search",
                            textAlign = TextAlign.Center,
                        )
                    }
                    
                }else{
                    LazyColumn(modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 5.dp)){

                        items(state.posts){ post ->
                            ArticleCard(
                                post = post,
                                onItemClick = {
                                    navHostController.navigate(Screens.POST_SCREEN + "/${post._id}/${Constants.API_SOURCE}")
                                },
                                onProfileNavigate = {
                                    viewModel.onProfileNavigate(it,bottomNavController,navHostController)
                                },
                                onDeletePost = {

                                },
                                isLiked = false,
                                isSaved = false,
                                isProfile = false,
                                profileImageUrl = "https://res.cloudinary.com/dhuqr5iyw/image/upload/v1640971757/mystory/profilepictures/default_y4mjwp.jpg"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(state.users){ user ->
                            ProfileCard(
                                navController = navHostController,
                                user =user,
                                onProfileNavigate = {
                                    viewModel.onProfileNavigate(it,bottomNavController,navHostController)
                                }
                            )
                        }
                    }
                }
                


            }
        }




    }


}