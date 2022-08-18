package com.peterchege.blogger.ui.author_profile

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.peterchege.blogger.components.ArticleCard
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Screens
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun AuthorProfileNavigation(

){
    val navController  = rememberNavController()
    NavHost(navController =navController, startDestination = Screens.AUTHOR_PROFILE_SCREEN +"/{username}" ){
        composable(Screens.AUTHOR_PROFILE_SCREEN + "/{username}"){
            AuthorProfileScreen(navController = navController)
        }
        composable(Screens.AUTHOR_FOLLOWER_FOLLOWING_SCREEN + "/{username}" + "/{type}"){
            AuthorFollowerFollowingScreen(navController = navController)
        }


    }
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun AuthorProfileScreen(
    navController: NavController,
    viewModel:AuthorProfileViewModel = hiltViewModel()
){
    val state = viewModel.state.value
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
        ,
    ) {
        if (viewModel.isLoading.value){
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }else{
            val gradient = Brush.linearGradient(0.0f to Color.White,0.0f to Color.White,start = Offset(0.0f, 200.0f),end = Offset(0.0f, 600.0f))
            if (viewModel.isFound.value){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(10.dp),
                    ){
                        item{
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.3f)

                                ){
                                    Image(
                                        modifier = Modifier
                                            .width(80.dp)
                                            .height(80.dp)
                                            .align(Alignment.Center)
                                        ,
                                        painter = rememberImagePainter(
                                            data = state.user?.imageUrl,
                                            builder = {
                                                crossfade(true)

                                            },
                                        ),
                                        contentDescription = "Profile Image")

                                }
                                Spacer(modifier = Modifier.height(5.dp))
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(
                                        text = state.user?.fullname ?: "",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp
                                    )
                                    Spacer(modifier = Modifier.padding(3.dp))
                                    Text(
                                        text = "@"+ (state.user?.username?.toLowerCase(Locale.ROOT)
                                            ?: ""),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))



                                }
                            }
                        }
                        item{
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally

                                ) {
                                    Text(
                                        text = "${state.posts.size}",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = "Articles",
                                        fontSize = 17.sp,
                                    )
                                }
                                Divider(color = Color.LightGray, thickness = 2.dp, modifier = Modifier
                                    .fillMaxHeight(0.7f)
                                    .width(1.dp))

                                Column(
                                    modifier = Modifier.clickable {
                                        navController.navigate(Screens.AUTHOR_FOLLOWER_FOLLOWING_SCREEN + "/${state.user?.username}"+ "/${Constants.FOLLOWER}")
                                    },
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        text = "${state.user?.followers?.size}"
                                    )
                                    Text(
                                        text = "Followers",
                                        fontSize = 17.sp,

                                        )
                                }
                                Divider(color = Color.LightGray, thickness = 2.dp, modifier = Modifier
                                    .fillMaxHeight(0.7f)
                                    .width(1.dp))

                                Column(
                                    modifier = Modifier.clickable {
                                        navController.navigate(Screens.AUTHOR_FOLLOWER_FOLLOWING_SCREEN + "/${state.user?.username}"+ "/${Constants.FOLLOWING}")
                                    },
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${state.user?.following?.size}",
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
                        item{
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                            ){
                                if(viewModel.isFollowing.value){
                                    Button(
                                        modifier = Modifier.fillMaxWidth(0.5f),
                                        onClick = {
                                            viewModel.unfollowUser()

                                        }) {
                                        Text(text = "Un Follow")
                                    }
                                }else{
                                    Button(
                                        modifier = Modifier.fillMaxWidth(0.5f),
                                        onClick = {
                                            viewModel.followUser()
                                        }) {
                                        Text(text = "Follow")
                                    }
                                }

                                Spacer(modifier = Modifier.width(5.dp))
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {

                                    }) {
                                    Text(text = "Message")
                                }

                            }
                        }
                        if (state.posts.isEmpty()) {
                            item{
                                Box(modifier = Modifier.fillMaxSize()){
                                    Text(
                                        modifier =Modifier.align(Alignment.Center),
                                        text = "This user does not have any posts yet"
                                    )
                                }
                            }
                        }else{
                            items(state.posts){ post ->
                                ArticleCard(
                                    post = post,
                                    onItemClick = {
                                        navController.navigate(Screens.POST_SCREEN +"/${post._id}/${Constants.API_SOURCE}")

                                    },
                                    onProfileNavigate ={ username ->

                                    } ,
                                    onDeletePost ={

                                    },
                                    profileImageUrl = state.user!!.imageUrl ,
                                    isLiked = false,
                                    isSaved = false,
                                    isProfile = true
                                )
                                Spacer(modifier = Modifier.padding(5.dp))
                            }
                        }

                    }
                }
            }else{
                Box(modifier = Modifier.fillMaxSize()){
                    Text(
                        modifier =Modifier.align(Alignment.Center),
                        text = "This user was not found"
                    )
                }
            }
        }
    }
}