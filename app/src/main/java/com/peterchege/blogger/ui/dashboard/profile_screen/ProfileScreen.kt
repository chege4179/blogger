package com.peterchege.blogger.ui.dashboard.profile_screen

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.peterchege.blogger.api.requests.CommentBody
import com.peterchege.blogger.components.ArticleCard
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Screens
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoilApi
@Composable
fun ProfileScreen(
    navController: NavController,
    navHostController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
){
    val scaffoldState = rememberScaffoldState()

    //val state = viewModel.state.value
    Scaffold(
        scaffoldState=scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
        ,
        topBar = {
            TopAppBar(
                title = {
                    viewModel.user.value?.let {
                        Text(
                            text= it.username,
                            )
                    }
                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }

    ) {


        if (viewModel.isLoading.value){
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }else{
            if (viewModel.isError.value){
                Box(modifier = Modifier.fillMaxSize()){
                    Text(
                        modifier =Modifier.align(Alignment.Center),
                        text = viewModel.msg.value
                    )

                }
            }else{
                viewModel.user.value?.let { user ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)

                            ,

                            ){
                            item{
                                Row(
                                    modifier = Modifier.fillMaxWidth(),

                                    ) {
                                    Image(
                                        modifier = Modifier
                                            .fillMaxWidth(0.3f)
                                            .height(60.dp),
                                        painter = rememberImagePainter(
                                            data = user.imageUrl,
                                            builder = {
                                                crossfade(true)

                                            },
                                        ),

                                        contentDescription = "")
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally

                                        ) {
                                            Text(
                                                text = "${viewModel.posts.value.size}",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                            Text(
                                                text = "Posts",
                                                fontSize = 17.sp,


                                                )
                                        }
                                        Column(
                                            modifier = Modifier.clickable {
                                                navController.navigate(Screens.PROFILE_FOLLOWER_FOLLOWING_SCREEN + "/${Constants.FOLLOWER}")

                                            },
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                text = "${viewModel.user.value?.followers?.size}"
                                            )
                                            Text(
                                                text = "Followers",
                                                fontSize = 17.sp,

                                                )
                                        }
                                        Column(
                                            modifier = Modifier.clickable {
                                                navController.navigate(Screens.PROFILE_FOLLOWER_FOLLOWING_SCREEN + "/${Constants.FOLLOWING}")
                                            },
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "${viewModel.user.value?.following?.size}",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                            Text(
                                                text = "Following",
                                                fontSize = 17.sp,

                                                )
                                        }


                                    }


                                }
                            }
                            item{
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = user.username)
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text = user.fullname)
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text = user.email)
                                    Spacer(modifier = Modifier.padding(5.dp))
                                }
                            }
                            item {
                                Box(modifier =  Modifier.fillMaxWidth()){
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        textDecoration = TextDecoration.Underline,
                                        text = "My Posts"
                                    )
                                }
                            }
                            items(viewModel.posts.value){ post ->
                                ArticleCard(
                                    post = post,
                                    onItemClick = {
                                        navHostController.navigate(Screens.POST_SCREEN +"/${post._id}/${Constants.API_SOURCE}")

                                    },
                                    onProfileNavigate ={ username ->

                                    } ,
                                    onDeletePost ={

                                    },

                                    profileImageUrl = user.imageUrl ,
                                    isLiked = false,
                                    isSaved = false,
                                    isProfile = true
                                )
                                Spacer(modifier = Modifier.padding(5.dp))

                            }
                            item{
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Text(text = "Profile Screen")
                                    Button(onClick = {
                                        viewModel.logoutUser(navHostController)
                                    }) {
                                        Text("Log out")

                                    }

                                }

                            }
                        }
                    }
                }

            }

        }
    }

}