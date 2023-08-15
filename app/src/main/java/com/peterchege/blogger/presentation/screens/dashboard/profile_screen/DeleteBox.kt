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
package com.peterchege.blogger.presentation.screens.dashboard.profile_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.presentation.screens.post_screen.PostScreenViewModel

@Composable
fun DeleteBox(
    modifier: Modifier = Modifier,
    post: Post,
    closeDeleteDialog: () -> Unit,
    deletePost: () -> Unit,

    ) {
    AlertDialog(
        onDismissRequest = {

        },
        title = {
            Text(text = "Delete '${post.postTitle}' ")
        },
        text = {
            Text(text = "Are you sure you want to delete this comment")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    deletePost()
                }
            ) {
                Text("Delete ")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    closeDeleteDialog()
                }
            ) {
                Text("Cancel")
            }
        }
    )

}