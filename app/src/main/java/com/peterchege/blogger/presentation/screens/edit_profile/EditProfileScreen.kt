/*
 * Copyright 2024 Blogger
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
package com.peterchege.blogger.presentation.screens.edit_profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.peterchege.blogger.R
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.presentation.components.AppLoader
import com.peterchege.blogger.presentation.components.CustomIconButton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditProfileScreen(
    viewModel: EditProfileScreenViewModel = hiltViewModel()
) {
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = authUser) {
        authUser?.let {
            viewModel.onChangeUserId(text = it.userId)
            viewModel.onChangeUsername(text = it.username)
            viewModel.onChangeFullName(text = it.fullName)
        }
    }

    EditProfileScreenContent(
        formState = formState,
        eventFlow = viewModel.eventFlow,
        onChangeUsername = viewModel::onChangeUsername,
        onChangeFullName = viewModel::onChangeFullName,
        onChangeImageUri = viewModel::onChangeImageUri,
        onSubmit = viewModel::onSaveProfile

    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreenContent(
    formState: EditProfileFormState,
    eventFlow: SharedFlow<UiEvent>,
    onChangeUsername: (String) -> Unit,
    onChangeFullName: (String) -> Unit,
    onChangeImageUri: (Uri) -> Unit,
    onSubmit: () -> Unit,

    ) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(it.message)

                }

                is UiEvent.Navigate -> {}
            }
        }

    }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            onChangeImageUri(it)
        }
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Edit Profile")
                }
            )
        }
    ) { paddingValues ->
        AppLoader(isLoading = formState.isLoading)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable {
                        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentAlignment = Alignment.Center
            ) {
                if (formState.imageUrl == null) {
                    CustomIconButton(
                        modifier = Modifier.align(Alignment.Center),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Profile Photo",
                        onClick = {
                            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    )
                } else {
                    AsyncImage(
                        model = formState.imageUrl,
                        modifier = Modifier.align(Alignment.Center),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Selected Image URL"
                    )

                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = formState.username,
                onValueChange = onChangeUsername,
                label = {
                    Text(text = stringResource(id = R.string.username))
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = formState.fullName,
                onValueChange = onChangeFullName,
                label = {
                    Text(text = stringResource(id = R.string.fullName))
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    keyboardController?.hide()
                    onSubmit()
                }
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}

@Preview
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreenContent(
        eventFlow = MutableSharedFlow(),
        formState = EditProfileFormState(isLoading = true),
        onChangeImageUri = {},
        onChangeFullName = {},
        onChangeUsername = {},
        onSubmit = {}
    )
}