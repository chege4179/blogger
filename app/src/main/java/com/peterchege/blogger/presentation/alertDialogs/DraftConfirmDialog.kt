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
                text = stringResource(id = R.string.save_draft_header_text),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.save_draft_text),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = onSaveDraftConfirm) {
                Text(text = stringResource(id = R.string.save).uppercase())
            }
        },
        dismissButton = {
            TextButton(
                onClick = onSaveDraftDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(text = stringResource(id = R.string.close).uppercase())
            }
        }
    )
}
