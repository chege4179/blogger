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
package com.peterchege.blogger.presentation.screens.edit_post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.blogger.R
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.util.UiEvent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditPostScreen(
    viewModel: EditPostScreenViewModel = hiltViewModel(),
    post: Post?,
) {
    val editPost by viewModel.editPost.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = post) {
        post?.let {
            viewModel.setEditPost(it)
        }

    }
    EditPostScreenContent(
        eventFlow = viewModel.eventFlow,
        post = editPost,
        onChangePostTitle = viewModel::onChangePostTitle,
        onChangePostBody = viewModel::onChangePostBody,
        updatePost = viewModel::updatePost
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreenContent(
    post: Post?,
    eventFlow: SharedFlow<UiEvent>,
    onChangePostTitle: (String) -> Unit,
    onChangePostBody: (String) -> Unit,
    updatePost: () -> Unit,
) {
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
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.edit_post_screen_header))
                }
            )
        }
    ) { paddingValues ->
        if (post == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text(
                    text = stringResource(id = R.string.edit_post_error),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(10.dp)
                    .verticalScroll(state = rememberScrollState())
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = post.postTitle,
                    maxLines = 3,
                    label = {
                        Text(text = stringResource(id = R.string.post_title))
                    },
                    onValueChange = {
                        onChangePostTitle(it)
                    })
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    value = post.postBody,
                    maxLines = 70,
                    label = {
                        Text(text = stringResource(id = R.string.post_body))
                    },
                    onValueChange = {
                        onChangePostBody(it)
                    }
                )
                Button(
                    onClick = { updatePost() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.update_button_text))
                }
            }
        }
    }


}