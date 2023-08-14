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
package com.peterchege.blogger.presentation.screens.dashboard.addpost_screen

//import androidx.compose.runtime.livedata.observeAsState
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.work.*
import coil.annotation.ExperimentalCoilApi
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.core.util.UiEvent

import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoilApi
@Composable
fun AddPostScreen(
    navigateBack:() -> Unit,
    navigateToDraftScreen: () -> Unit,
    navigateToDashboardScreen:() -> Unit,
    viewModel: AddPostScreenViewModel = hiltViewModel(),

    ){
    val isUploading by viewModel.isUploading.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()

    AddPostScreenContent(
        formState = formState,
        isUploading = isUploading,
        eventFlow = viewModel.eventFlow,
        onChangePostTitle = { viewModel.onChangePostTitle(it) },
        onChangePostBody = { viewModel.onChangePostBody(it) },
        onChangeImageUri = { viewModel.onChangePhotoUri(it) },
        onSubmit = { viewModel.postArticle(navigateToDashboardScreen = navigateToDashboardScreen,user = authUser) },
        navigateToDraftScreen = navigateToDraftScreen,
        onBackPress = { viewModel.onBackPress(navigateToDashboardScreen) },
        onSaveDraftConfirm = { viewModel.onSaveDraftConfirm(navigateBack) },
        onSaveDraftDismiss = { viewModel.onSaveDraftDismiss(navigateBack) },
        onCloseDialog = { viewModel.onCloseDialog() }
    )



}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalCoilApi
@Composable
fun AddPostScreenContent(
    navigateToDraftScreen:() -> Unit,
    formState: AddPostFormState,
    isUploading:Boolean,
    eventFlow:SharedFlow<UiEvent>,
    onChangePostTitle:(String) -> Unit,
    onChangePostBody:(String) -> Unit,
    onChangeImageUri: (Uri?) -> Unit,
    onSubmit:() -> Unit,
    onBackPress:() -> Unit,
    onSaveDraftConfirm: () -> Unit,
    onSaveDraftDismiss: () -> Unit,
    onCloseDialog: () -> Unit


    ) {
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onChangeImageUri(uri)
        }

    }
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.Navigate -> {

                }
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState
    ) {

        BackHandler() {
            onBackPress()
        }
        if (formState.isSaveDraftModalOpen) {
            DraftConfirmBox(
                onSaveDraftDismiss = onSaveDraftDismiss,
                onSaveDraftConfirm = onSaveDraftConfirm,
                onCloseDialog = onCloseDialog,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),

            ) {
            LazyColumn() {

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly

                    ) {
                        formState.uri?.let {
                            GlideImage(
                                imageModel = { it },
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(135.dp),

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
                                    onChangeImageUri(null)
                                }) {
                                Text("Remove Image")
                            }
                        }

                    }
                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = formState.postTitle,
                            maxLines = 3,
                            label = {
                                Text("Post Title")
                            },
                            onValueChange = {
                                onChangePostTitle(it)
                            })
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            value = formState.postBody,
                            maxLines = 70,
                            label = {
                                Text("Write your own story")
                            },
                            onValueChange = {
                                onChangePostBody(it)
                            })
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Button(
                                onClick = {
                                    navigateToDraftScreen()

                                }) {
                                Text(
                                    text = "Go To Drafts"
                                )
                            }
                            Button(
                                onClick = {
                                    onSubmit()
                                }) {
                                Text(
                                    text = "Post")
                            }
                        }

                    }
                }
            }
            if (isUploading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}


@Composable
fun DraftConfirmBox(
    modifier: Modifier = Modifier,
    onSaveDraftConfirm:() -> Unit,
    onSaveDraftDismiss:() -> Unit,
    onCloseDialog:() -> Unit,

) {

    val context = LocalContext.current

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onCloseDialog()
        },
        title = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Save Draft",
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
                        onCloseDialog()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.onSurface
                    )
                ) {
                    Text(text = "Close".uppercase())
                }
                TextButton(
                    modifier = buttonModifier,
                    onClick = {
                        onSaveDraftDismiss()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.onSurface
                    )
                ) {
                    Text(text = "Don't Save".uppercase())
                }
                TextButton(
                    modifier = buttonModifier,
                    onClick = {
                        onSaveDraftConfirm()
                    }
                ) {
                    Text(text = "Save ".uppercase())
                }
            }
        }
    )
}