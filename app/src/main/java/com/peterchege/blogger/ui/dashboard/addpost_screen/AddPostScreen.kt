package com.peterchege.blogger.ui.dashboard.addpost_screen

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.decodeBitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.work.*
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.util.*
import com.skydoves.landscapist.glide.GlideImage


@ExperimentalCoilApi
@Composable
fun AddPostScreen(
    navController: NavController,
    bottomNavController: NavController,
    viewModel: AddPostScreenViewModel = hiltViewModel(),

){
    val context  = LocalContext.current
    val state = viewModel.state.value
    val imageUrlState = viewModel.imageUrlState.value
    val postTitle = viewModel.postTitle.value
    val postBody = viewModel.postBody.value
    var bitmapState = viewModel.bitmapState.value
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()){ uri: Uri? ->
        if (uri != null) {
            viewModel.onChangePhotoUri(uri, context = context)
        }

    }
    val scaffoldState = rememberScaffoldState()
    val postArticleRequest = OneTimeWorkRequestBuilder<UploadPostWorker>()
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(
                    NetworkType.CONNECTED
                )
                .build()
        )
        .build()

    val workManager = WorkManager.getInstance(context)
//    val workInfos = workManager
//        .getWorkInfosForUniqueWorkLiveData("post_article")
//        .observeAsState()
//        .value
    Scaffold(
        scaffoldState = scaffoldState

    ) {

        BackHandler() {
            viewModel.onBackPress(scaffoldState, navController = navController)
        }
        if (viewModel.openSaveDraftModal.value){
            DraftConfirmBox(
                scaffoldState = scaffoldState,
                navController = navController,
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),

            ) {
            LazyColumn() {

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly

                    ){
                        imageUrlState?.let {
                            GlideImage(
                                imageModel = it,
                                modifier= Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(135.dp),
                                contentScale = ContentScale.Crop
                                )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.2f),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Button(
                                modifier = Modifier.fillMaxWidth(0.95f),
                                onClick = {
                                    launcher.launch("image/*")
                                }) {
                                Text("Select Image")
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                modifier = Modifier.fillMaxWidth(0.95f),
                                onClick = {
                                    viewModel.onChangePhotoUri(null,context)
                                }) {
                                Text("Remove Image")
                            }
                        }

                    }
                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier= Modifier.fillMaxSize()
                    ){
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = postTitle.text  ,
                            maxLines = 3,
                            label= {
                                Text("Post Title")
                            },
                            onValueChange ={
                                viewModel.onChangePostTitle(it)
                            })
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            value = postBody.text,
                            maxLines = 70,
                            label = {
                                Text("Write your own story")
                            },
                            onValueChange = {
                                viewModel.onChangePostBody(it)
                            })
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Button(
                                onClick = {
                                navController.navigate(Screens.DRAFT_SCREEN)
                            }) {
                                Text("Go To Drafts")
                            }
                            Button(
                                onClick = {
                                    viewModel.postArticle(bottomNavController,scaffoldState,context)
                            }) {
                                Text("Post")
                            }
                        }

                    }
                }
            }
            if (state.isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}


@Composable
fun DraftConfirmBox(
    modifier: Modifier = Modifier,
    viewModel: AddPostScreenViewModel = hiltViewModel(),
    scaffoldState : ScaffoldState,
    navController: NavController

    ) {

    val context = LocalContext.current

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            viewModel.onSaveDraftDismiss(navController = navController)
        },
        title = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Save Draft"  ,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Do you want to save this draft ?",

                    textAlign = TextAlign.Center
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                val buttonModifier = Modifier.align(Alignment.CenterVertically)
                TextButton(
                    modifier = buttonModifier,
                    onClick = {
                        viewModel.onSaveDraftDismiss(navController = navController)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.onSurface
                    )
                ) {
                    Text(text = "Cancel".uppercase())
                }
                TextButton(
                    modifier = buttonModifier,
                    onClick = {
                        viewModel.onSaveDraftConfirm(scaffoldState = scaffoldState, navController = navController)

                    }
                ) {
                    Text(text = "Save ".uppercase())
                }
            }
        }
    )
}