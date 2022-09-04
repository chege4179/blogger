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
import com.peterchege.blogger.components.ArticleCard
import com.peterchege.blogger.ui.search_screen.SearchProductScreenViewModel
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Screens

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchPostsTab(
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
            if (viewModel.searchPosts.value.isEmpty()){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "No posts found")
                }
            }else{
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
                ) {
                    items(viewModel.searchPosts.value) { post ->
                        ArticleCard(
                            post = post,
                            onItemClick = {
                                navHostController.navigate(Screens.POST_SCREEN + "/${post._id}/${Constants.API_SOURCE}")
                            },
                            onProfileNavigate = {
                                viewModel.onProfileNavigate(it,navHostController,navHostController)
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
                }
            }
        }



    }
}