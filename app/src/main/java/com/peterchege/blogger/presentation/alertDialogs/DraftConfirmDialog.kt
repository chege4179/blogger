package com.peterchege.blogger.presentation.alertDialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign


@Composable
fun DraftConfirmDialog(
    modifier: Modifier = Modifier,
    onSaveDraftConfirm: () -> Unit,
    onSaveDraftDismiss: () -> Unit,
    onCloseDialog: () -> Unit,
    ) {

    AlertDialog(
        onDismissRequest = {
            onCloseDialog()
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Save Draft",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Do you want to save this draft ?",

                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(

                onClick = {
                    onSaveDraftConfirm()
                }
            ) {
                Text(text = "Save ".uppercase())
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onSaveDraftDismiss()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(text = "Close".uppercase())
            }
        }
    )
}
