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
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.peterchege.blogger.presentation.screens.post_screen.PostViewModel
import java.util.*

@Composable
fun DeleteBox(
    modifier: Modifier = Modifier,
    viewModel: PostViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,

    ) {

    val context = LocalContext.current


    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            viewModel.onDialogDeleteDismiss()
        },
        text = {
            Column {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Delete" + "${viewModel.state.value.post?.postTitle}",
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Are you sure you want to delete this post ?",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                val buttonModifier = Modifier.align(Alignment.CenterVertically)
                TextButton(
                    modifier = buttonModifier,
                    onClick = {
                        viewModel.onDialogDismiss()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.onSurface
                    )
                ) {
                    Text(text = "Cancel".uppercase())
                }
                TextButton(
                    modifier = buttonModifier,
                    onClick = {
                        viewModel.onDialogDeleteConfirm(scaffoldState = scaffoldState)

                    }
                ) {
                    Text(text = "Delete".uppercase())
                }
            }
        }
    )
}