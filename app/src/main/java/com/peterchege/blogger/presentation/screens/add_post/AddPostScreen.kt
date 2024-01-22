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
package com.peterchege.blogger.presentation.screens.add_post


import android.annotation.SuppressLint
import android.content.*
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.*
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import com.peterchege.blogger.core.services.UploadPostService
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.presentation.alertDialogs.DraftConfirmDialog
import com.peterchege.blogger.presentation.components.NotLoggedInComponent
import com.peterchege.blogger.presentation.theme.defaultPadding
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoilApi
@Composable
fun AddPostScreen(
    navigateBack: () -> Unit,
    navigateToDraftScreen: () -> Unit,
    navigateToDashboardScreen: () -> Unit,
    navigateToLoginScreen:() -> Unit,
    navigateToSignUpScreen:() -> Unit,
    viewModel: AddPostScreenViewModel = hiltViewModel(),

    ) {
    val isUploading by viewModel.isUploading.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    AddPostScreenContent(
        uiState = uiState,
        formState = formState,
        isUploading = isUploading,
        eventFlow = viewModel.eventFlow,
        onChangePostTitle = viewModel::onChangePostTitle,
        onChangePostBody = viewModel::onChangePostBody,
        onChangeImageUri = viewModel::onChangePhotoUri ,
        onSubmit = {
            authUser?.let { user ->
                if (viewModel.formState.value.uri == null){
                    viewModel.emitSnackBarEvent(msg = "Select a blogger image")
                    return@let
                }
                if (viewModel.formState.value.postBody ==""){
                    viewModel.emitSnackBarEvent(msg ="Post body can't be empty")
                    return@let
                }
                if (viewModel.formState.value.postTitle ==""){
                    viewModel.emitSnackBarEvent(msg ="Post Title can't be empty")
                    return@let
                }
                Intent(context,UploadPostService::class.java).also {
                    it.action = UploadPostService.Actions.START.toString()
                    it.putExtra("postTitle",viewModel.formState.value.postTitle)
                    it.putExtra("postBody",viewModel.formState.value.postBody)
                    it.putExtra("userId",user.userId)
                    it.putExtra("uri",viewModel.formState.value.uri.toString())
                    context.startService(it)
                }
                navigateToDashboardScreen()

            }
        },
        navigateToDraftScreen = navigateToDraftScreen,
        onBackPress = { viewModel.onBackPress(navigateToDashboardScreen) },
        onSaveDraftConfirm = { viewModel.onSaveDraftConfirm(navigateBack) },
        onSaveDraftDismiss = { viewModel.onSaveDraftDismiss(navigateBack) },
        onCloseDialog = { viewModel.onCloseDialog() },
        navigateToLoginScreen = navigateToLoginScreen,
        navigateToSignUpScreen = navigateToSignUpScreen,
    )


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalCoilApi
@Composable
fun AddPostScreenContent(
    uiState: AddPostScreenUiState,
    navigateToDraftScreen: () -> Unit,
    formState: AddPostFormState,
    isUploading: Boolean,
    eventFlow: SharedFlow<UiEvent>,
    onChangePostTitle: (String) -> Unit,
    onChangePostBody: (String) -> Unit,
    onChangeImageUri: (Uri?) -> Unit,
    onSubmit: () -> Unit,
    onBackPress: () -> Unit,
    onSaveDraftConfirm: () -> Unit,
    onSaveDraftDismiss: () -> Unit,
    onCloseDialog: () -> Unit,
    navigateToLoginScreen:() -> Unit,
    navigateToSignUpScreen:() -> Unit,


    ) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onChangeImageUri(uri)
        }

    }
    val snackbarHostState = remember{ SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.Navigate -> {

                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) {

        BackHandler {
            onBackPress()
        }
        if (formState.isSaveDraftModalOpen) {
            DraftConfirmDialog(
                onSaveDraftDismiss = onSaveDraftDismiss,
                onSaveDraftConfirm = onSaveDraftConfirm,
                onCloseDialog = onCloseDialog,
            )
        }
        when(uiState){
            is AddPostScreenUiState.NotLoggedIn -> {
                NotLoggedInComponent(
                    navigateToLoginScreen = navigateToLoginScreen,
                    navigateToSignUpScreen =navigateToSignUpScreen
                )
            }
            is AddPostScreenUiState.LoggedIn -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(defaultPadding)
                    ,
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly

                            ) {
                                formState.uri?.let {
                                    SubcomposeAsyncImage(
                                        model = it ,
                                        modifier = Modifier
                                            .width(135.dp)
                                            .height(135.dp)
                                        ,
                                        contentDescription = "Add Post Image",
                                        contentScale = ContentScale.FillBounds
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
                                            text = "Post"
                                        )
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

    }
}

