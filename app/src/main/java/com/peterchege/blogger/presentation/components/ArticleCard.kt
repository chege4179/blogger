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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.presentation.theme.LightIconColor


@ExperimentalCoilApi
@Composable
fun ArticleCard(
    post: Post,
    onItemClick: (Post) -> Unit,
    onProfileNavigate: (String) -> Unit,
    onDeletePost: (Post) -> Unit,

    isLiked: Boolean,
    isSaved: Boolean,
    isProfile: Boolean,
    onBookmarkPost:(Post) -> Unit = {  },
    onUnBookmarkPost:(Post) -> Unit = {  },
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(15.dp), clip = true)
            .clickable { onItemClick(post) },
        shape = RoundedCornerShape(15.dp),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = LightIconColor)
        ) {
            post.let {
                SubcomposeAsyncImage(
                    model = post.imageUrl,
                    loading = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .height(150.dp),
                    contentDescription = "Post Image"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.8f)

                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = post.postTitle,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,


                            )
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "By : ",
                                modifier = Modifier
                                    .padding(vertical = 7.dp)
//                                    .clickable {
//                                        onProfileNavigate(post.postAuthor)
//                                    }
                            )
                            Text(
                                text = post.postAuthor,
                                modifier = Modifier
                                    .padding(vertical = 7.dp)
                                    .clickable {
                                        onProfileNavigate(post.postAuthor)
                                    },
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp

                            )
                        }


                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .fillMaxHeight()
                    ) {
                        if (isProfile) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Share",
                                modifier = Modifier.clickable {

                                }
                            )
                        } else {
                            Icon(
                                imageVector =if(isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                modifier = Modifier.clickable {

                                }
                            )
                            Icon(
                                imageVector = if(isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Saved",
                                modifier = Modifier.clickable {
                                    if(isSaved){
                                        onUnBookmarkPost(post)
                                    }else{
                                        onBookmarkPost(post)
                                    }

                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
