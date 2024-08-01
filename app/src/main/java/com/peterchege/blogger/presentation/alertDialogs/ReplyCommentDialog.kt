package com.peterchege.blogger.presentation.alertDialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.peterchege.blogger.R
import com.peterchege.blogger.core.api.responses.models.CommentWithUser
import com.peterchege.blogger.presentation.screens.post.CommentUiState

@Composable
fun ReplyCommentDialog(
    isUserLoggedIn: Boolean,
    modifier: Modifier = Modifier,
    commentUiState: CommentUiState,
    onChangeNewComment: (String) -> Unit,
    replyToComment: () -> Unit,
    closeCommentDialog: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            closeCommentDialog()
        },
        title = {
            Text(text = "Reply to '${commentUiState.commentToBeRepliedTo?.message ?: ""}'")
        },
        text = {
            if (isUserLoggedIn) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = commentUiState.replyCommentMessage,
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
                Text(text = stringResource(id = R.string.login_to_comment_text))
            }

        },
        confirmButton = {
            TextButton(
                onClick = replyToComment
            ) {
                Text(text = stringResource(id = R.string.comment))
            }
        },
        dismissButton = {
            TextButton(onClick = closeCommentDialog) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}
