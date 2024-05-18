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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.*
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import com.peterchege.blogger.R
import com.peterchege.blogger.core.services.UploadPostService
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.core.util.isNotNull
import com.peterchege.blogger.core.util.isNull
import com.peterchege.blogger.presentation.alertDialogs.DraftConfirmDialog
import com.peterchege.blogger.presentation.components.CustomIconButton
import com.peterchege.blogger.presentation.components.NotLoggedInComponent
import com.peterchege.blogger.presentation.theme.BloggerTheme
import com.peterchege.blogger.presentation.theme.defaultPadding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoilApi
@Composable
fun AddPostScreen(
    navigateBack: () -> Unit,
    navigateToDraftScreen: () -> Unit,
    navigateToDashboardScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
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
        onChangeImageUri = viewModel::onChangePhotoUri,
        onSubmit = {
            authUser?.let { user ->
                if (viewModel.formState.value.uri == null) {
                    viewModel.emitSnackBarEvent(msg = context.getString(R.string.post_image_uri_error))
                    return@let
                }
                if (viewModel.formState.value.postBody == "") {
                    viewModel.emitSnackBarEvent(msg = context.getString(R.string.post_body_error))
                    return@let
                }
                if (viewModel.formState.value.postTitle == "") {
                    viewModel.emitSnackBarEvent(msg = context.getString(R.string.post_title_error))
                    return@let
                }
                Intent(context, UploadPostService::class.java).also {
                    it.action = UploadPostService.Actions.START.toString()
                    it.putExtra("postTitle", viewModel.formState.value.postTitle)
                    it.putExtra("postBody", viewModel.formState.value.postBody)
                    it.putExtra("userId", user.userId)
                    it.putExtra("uri", viewModel.formState.value.uri.toString())
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

@OptIn(ExperimentalMaterial3Api::class)
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
    navigateToLoginScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,


    ) {
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            onChangeImageUri(it)
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

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
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.create_post_icon_description),
                        textAlign = TextAlign.Center,)
                }
            )
        }
    ) { paddingValues ->

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
        when (uiState) {
            is AddPostScreenUiState.NotLoggedIn -> {
                NotLoggedInComponent(
                    navigateToLoginScreen = navigateToLoginScreen,
                    navigateToSignUpScreen = navigateToSignUpScreen
                )
            }

            is AddPostScreenUiState.LoggedIn -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(defaultPadding),
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly

                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(120.dp)
                                        .border(BorderStroke(1.dp, Color.Gray))
                                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AnimatedContent(
                                        targetState = formState.uri.isNotNull(),
                                        label = "Post Image"
                                    ) { isUriNull ->
                                        if (isUriNull) {
                                            SubcomposeAsyncImage(
                                                model = formState.uri,
                                                modifier = Modifier
                                                    .width(135.dp)
                                                    .height(135.dp),
                                                contentDescription = stringResource(id = R.string.add_post_image_text),
                                                contentScale = ContentScale.FillBounds
                                            )
                                        } else {
                                            CustomIconButton(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .align(Alignment.Center),
                                                imageVector = Icons.Default.Add,
                                                contentDescription = stringResource(id = R.string.add_post_image_text),
                                                onClick = {
                                                    photoPicker.launch(
                                                        PickVisualMediaRequest(
                                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                    }

                                }

                                Column(
                                    modifier = Modifier
                                        .height(120.dp)
                                        .padding(5.dp),
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Button(
                                        modifier = Modifier.fillMaxWidth(0.95f),
                                        shape = MaterialTheme.shapes.small,
                                        onClick = {
                                            photoPicker.launch(
                                                PickVisualMediaRequest(
                                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                                )
                                            )
                                        }
                                    ) {
                                        Text(text = stringResource(id = R.string.select_image_text))
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(
                                        modifier = Modifier.fillMaxWidth(0.95f),
                                        shape = MaterialTheme.shapes.small,
                                        onClick = {
                                            onChangeImageUri(null)
                                        }
                                    ) {
                                        Text(text = stringResource(id = R.string.remove_image_text))
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
                                        Text(text = stringResource(id = R.string.post_title))
                                    },
                                    onValueChange = onChangePostTitle
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(400.dp),
                                    value = formState.postBody,
                                    maxLines = 70,
                                    label = {
                                        Text(text = stringResource(id = R.string.post_body))
                                    },
                                    onValueChange = onChangePostBody
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Button(
                                        shape = MaterialTheme.shapes.small,
                                        onClick = navigateToDraftScreen
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.view_draft)
                                        )
                                    }
                                    Button(
                                        shape = MaterialTheme.shapes.small,
                                        onClick = onSubmit
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.post)
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

@Preview(
    showSystemUi = true,
)
@Composable
fun AddPostScreenPreview1() {
    AddPostScreenContent(
        uiState = AddPostScreenUiState.LoggedIn,
        navigateToDraftScreen = { /*TODO*/ },
        formState = AddPostFormState(),
        isUploading = false,
        eventFlow = MutableSharedFlow(),
        onChangePostTitle = {},
        onChangePostBody = {},
        onChangeImageUri = {},
        onSubmit = { /*TODO*/ },
        onBackPress = { /*TODO*/ },
        onSaveDraftConfirm = { /*TODO*/ },
        onSaveDraftDismiss = { /*TODO*/ },
        onCloseDialog = { /*TODO*/ },
        navigateToLoginScreen = { /*TODO*/ },
        navigateToSignUpScreen = {}
    )
}

@Preview(
    showSystemUi = true,
)
@Composable
fun AddPostScreenPreview2() {
    AddPostScreenContent(
        uiState = AddPostScreenUiState.LoggedIn,
        navigateToDraftScreen = { /*TODO*/ },
        formState = AddPostFormState(isSaveDraftModalOpen = true),
        isUploading = false,
        eventFlow = MutableSharedFlow(),
        onChangePostTitle = {},
        onChangePostBody = {},
        onChangeImageUri = {},
        onSubmit = { /*TODO*/ },
        onBackPress = { /*TODO*/ },
        onSaveDraftConfirm = { /*TODO*/ },
        onSaveDraftDismiss = { /*TODO*/ },
        onCloseDialog = { /*TODO*/ },
        navigateToLoginScreen = { /*TODO*/ },
        navigateToSignUpScreen = {}
    )

}

@Preview
@Composable
fun AddPostScreenPreview3() {
    BloggerTheme(darkTheme = true) {
        AddPostScreenContent(
            uiState = AddPostScreenUiState.LoggedIn,
            navigateToDraftScreen = { /*TODO*/ },
            formState = AddPostFormState(),
            isUploading = false,
            eventFlow = MutableSharedFlow(),
            onChangePostTitle = {},
            onChangePostBody = {},
            onChangeImageUri = {},
            onSubmit = { /*TODO*/ },
            onBackPress = { /*TODO*/ },
            onSaveDraftConfirm = { /*TODO*/ },
            onSaveDraftDismiss = { /*TODO*/ },
            onCloseDialog = { /*TODO*/ },
            navigateToLoginScreen = { /*TODO*/ },
            navigateToSignUpScreen = {}
        )
    }

}

