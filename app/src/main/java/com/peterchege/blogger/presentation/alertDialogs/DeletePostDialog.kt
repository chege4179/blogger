package com.peterchege.blogger.presentation.alertDialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.peterchege.blogger.core.api.responses.models.Post

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
            Text(text = "Delete '${post.postTitle}' ")
        },
        text = {
            Text(text = "Are you sure you want to delete this post")
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