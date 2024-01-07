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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.peterchege.blogger.presentation.screens.post.CommentUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentDialog(
    isUserLoggedIn: Boolean,
    modifier: Modifier = Modifier,
    commentUiState: CommentUiState,
    onChangeNewComment: (String) -> Unit,
    postComment: () -> Unit,
    closeCommentDialog: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            closeCommentDialog()
        },
        title = {
            Text(text = "Post Comment")
        },
        text = {
            if (isUserLoggedIn) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = commentUiState.newComment,
                    onValueChange = {
                        onChangeNewComment(it)
                    },
                    singleLine = false,
                    textStyle = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Start),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {

                        }
                    )
                )
            } else {
                Text(text = "Log In to be able to comment")
            }

        },
        confirmButton = {
            TextButton(
                onClick = {
                    postComment()
                }
            ) {
                Text(text = "Comment")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    closeCommentDialog()
                }
            ) {
                Text(text = "Cancel")
            }
        }
    )
}
