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
package com.peterchege.blogger.presentation.alertDialogs

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.peterchege.blogger.core.api.responses.models.CommentWithUser
import com.peterchege.blogger.core.api.responses.models.Post

@Composable
fun DeleteCommentDialog(
    comment: CommentWithUser,
    closeDeleteDialog: () -> Unit,
    deleteComment: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            closeDeleteDialog()
        },
        title = {
            Text(text = "Delete '${comment.message}' ")
        },
        text = {
            Text(text = "Are you sure you want to delete this comment")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    deleteComment()
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