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
package com.peterchege.blogger.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterchege.blogger.core.room.entities.DraftPost
import com.peterchege.blogger.core.util.truncateString

@Preview
@Composable
fun DraftCardPreview() {
    DraftCard(
        navigateToAddPostScreen = { },
        draftRecord = DraftPost(
            id = null,
            postTitle = "Draft Post",
            postBody = truncateString("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",50)

        ),
        onDeleteDraft = { }
    )
}

@Composable
fun DraftCard(
    navigateToAddPostScreen: (Int) -> Unit,
    draftRecord: DraftPost,
    onDeleteDraft: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                draftRecord.id?.let {
                    navigateToAddPostScreen(it)
                }

            },
        ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 1.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = draftRecord.postTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = truncateString(draftRecord.postBody,50),
                )
            }
            CustomIconButton(
                imageVector = Icons.Default.Delete,
                onClick = {
                    draftRecord.id?.let {
                        onDeleteDraft(it)
                    }
                },
                contentDescription = "Delete Draft "
            )


        }
    }

}