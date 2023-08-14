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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.peterchege.blogger.core.room.entities.DraftRecord
import com.peterchege.blogger.core.util.Screens


@Composable
fun DraftCard(
    navigateToAddPostScreen:(Int) -> Unit,
    draftRecord: DraftRecord,
    onDeleteDraft:(Int) -> Unit
){
    Card(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(60.dp)
            .border(BorderStroke(1.dp, Color.DarkGray))
            .clickable {
                navigateToAddPostScreen(draftRecord.id ?: 0)

            }
        ,

    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 3.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(0.75f)
                    .padding(horizontal = 5.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = draftRecord.postTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = draftRecord.postBody,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = {
                        draftRecord.id?.let { onDeleteDraft(it) }

                    }) {
                    Text(text = "Delete ")

                }
            }

        }
    }

}