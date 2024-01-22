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
package com.peterchege.blogger.presentation.alertDialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.R

@Composable
fun DeletePostDialog(
    post: Post,
    closeDeleteDialog: () -> Unit,
    deletePost: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            closeDeleteDialog()
        },
        title = {
            Text(text = "${stringResource(id = R.string.delete)} '${post.postTitle}' ")
        },
        text = {
            Text(text = stringResource(id = R.string.delete_post_description_text))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    deletePost()
                }
            ) {
                Text(text = stringResource(id = R.string.delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    closeDeleteDialog()
                }
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )

}