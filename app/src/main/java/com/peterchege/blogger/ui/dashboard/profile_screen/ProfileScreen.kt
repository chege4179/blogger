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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import com.peterchege.blogger.components.BottomSheetItem
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Screens
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalCoilApi
@Composable
fun ProfileScreen(
    navController: NavController,
    navHostController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
){
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()

    //val state = viewModel.state.value
    BottomSheetScaffold(
        scaffoldState=scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
        ,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.LightGray)
                ,
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    BottomSheetItem(name = "Log Out", onClick = {
                        viewModel.logoutUser(navHostController)
                    })

                }
            }
        },
        sheetBackgroundColor = Color.White,
        sheetPeekHeight = 0.dp,
//        topBar = {
//            TopAppBar(
//                title = {
//                    viewModel.user.value?.let {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 20.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.CenterVertically,
//                        ){
//                            Text(
//                                text= it.username,
//                            )
//                            Icon(
//                                Icons.Filled.Settings,
//                                contentDescription = "Settings",
//                                Modifier
//                                    .size(26.dp)
//                                    .clickable {
//                                        scope.launch {
//                                            if (sheetState.isCollapsed) {
//                                                sheetState.expand()
//                                            } else {
//                                                sheetState.collapse()
//                                            }
//                                        }
//                                    }
//                            )
//                        }
//
//                    }
//                }
//                ,
//                backgroundColor = MaterialTheme.colors.primary)
//        }

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
                                Column(
                                    modifier = Modifier.fillMaxWidth().height(200.dp),
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
                                                data = user.imageUrl,
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
                                            text = user.fullname,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp
                                        )
                                        Spacer(modifier = Modifier.padding(3.dp))
                                        Text(
                                            text = "@"+user.username,
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
                                            text = "${viewModel.posts.value.size}",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text(
                                            text = "Posts",
                                            fontSize = 17.sp,
                                            )
                                    }
                                    Divider(color = Color.LightGray, thickness = 2.dp, modifier = Modifier.fillMaxHeight(0.7f).width(1.dp))

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
                                    Divider(color = Color.LightGray, thickness = 2.dp, modifier = Modifier.fillMaxHeight(0.7f).width(1.dp))

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
                        }
                    }
                }
            }
        }
    }

}