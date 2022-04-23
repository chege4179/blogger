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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.peterchege.blogger.components.ArticleCard
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Screens


@Composable
fun AuthorProfileNavigation(

){

    val navController  = rememberNavController()
    NavHost(navController =navController, startDestination =Screens.AUTHOR_PROFILE_SCREEN +"/{username}" ){
        composable(Screens.AUTHOR_PROFILE_SCREEN + "/{username}"){
            AuthorProfileScreen(navController = navController)
        }
        composable(Screens.FOLLOWER_FOLLOWING_SCREEN + "/{type}"){
            FollowerFollowingScreen(navController = navController)
        }


    }
}
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
        topBar = {
            TopAppBar(
                title = {
                    state.user?.let {
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
            if (viewModel.isFound.value){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)

                        ,

                        ){
                        state.user?.let {
                            item{
                                Row(
                                    modifier = Modifier.fillMaxWidth(),

                                    ) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(0.3f)
                                    ){
                                        Image(
                                            modifier = Modifier
                                                .width(80.dp)
                                                .height(80.dp),
                                            painter = rememberImagePainter(
                                                data = state.user?.imageUrl,
                                                builder = {
                                                    crossfade(true)

                                                },
                                            ),

                                            contentDescription = "Author Profile Image URL")
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth().height(70.dp),
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
                                                text = "${state.user?.followers?.size}"
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
                            }
                            item{
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = state.user.username)
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text = state.user.fullname)
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text = state.user.email)
                                    Spacer(modifier = Modifier.padding(5.dp))
                                }
                            }
                            item {
                                Row(

                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,

                                ) {
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
                                            Text(text = " Follow")
                                        }
                                    }

                                    Spacer(modifier = Modifier.padding(8.dp))
                                    Button(
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = {

                                        }) {
                                        Text(text = "Message")
                                    }
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
                            items(state.posts){ post ->
                                ArticleCard(
                                    post = post,
                                    onItemClick = {
                                        navController.navigate(Screens.POST_SCREEN +"/${post._id}/${Constants.API_SOURCE}")


                                    },
                                    onDeletePost = {

                                    },
                                    onProfileNavigate ={ username ->

                                    } ,
                                    profileImageUrl = state.user!!.imageUrl ,
                                    isLiked = false,
                                    isSaved = false,
                                    isProfile = false,
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