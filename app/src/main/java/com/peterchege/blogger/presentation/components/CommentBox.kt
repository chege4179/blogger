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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.peterchege.blogger.core.api.responses.Comment


@ExperimentalCoilApi
@Composable
fun CommentBox(
    comment: Comment,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),

            ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.15f)
                    .height(60.dp),
                painter = rememberImagePainter(
                    data = comment.imageUrl,
                    builder = {
                        crossfade(true)

                    },
                ),

                contentDescription = comment.comment
            )
            Column(
                Modifier.fillMaxHeight(0.1f)

            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    text = comment.username,
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .clickable {  }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 60.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(5.dp)


                ) {
                    Text(
                        text = comment.comment,
                        textAlign = TextAlign.Start,
                        textDecoration = TextDecoration.None,
                        color = Color.Black,
                    )

                }
            }


        }

    }

}