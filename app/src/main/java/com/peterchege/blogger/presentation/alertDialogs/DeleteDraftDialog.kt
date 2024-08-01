package com.peterchege.blogger.presentation.alertDialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.peterchege.blogger.R
import com.peterchege.blogger.core.room.entities.DraftPost


@Composable
fun DeleteDraftDialog(
    draftPost: DraftPost,
    modifier: Modifier = Modifier,
    onDeleteDraftConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.delete_draft_header_text) + ": ${draftPost.postTitle}",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Left
            )
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.delete_draft_text),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = onDeleteDraftConfirm) {
                Text(text = stringResource(id = R.string.delete).uppercase())
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(text = stringResource(id = R.string.close).uppercase())
            }
        }
    )
}