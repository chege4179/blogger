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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.PostAuthor
import com.peterchege.blogger.core.api.responses.models.PostCount
import com.peterchege.blogger.presentation.theme.LightIconColor
import com.peterchege.blogger.R

@OptIn(ExperimentalCoilApi::class)
@Preview(
    showSystemUi = true,

)
@Composable
fun ArticleCardPreview() {
    ArticleCard(
        post = Post(
            postId = "",
            postTitle = "djdjdj",
            postBody = "",
            postAuthorId = "",
            createdAt = "",
            updatedAt = "",
            imageUrl = "http://res.cloudinary.com/dhuqr5iyw/image/upload/v1705085833/instamall/6a15b8866642626f0c1e6d69ceab2240.jpg",
            postAuthor = PostAuthor(
                userId = "",
                email = "",
                username = "",
                password = "",
                createdAt = "",
                updatedAt = "",
                imageUrl = "",
                fullName = "Peter Chege"
            ),
            count = PostCount(
                likes = 0,
                views = 0,
                comments = 0
            )
        ),
        onItemClick = { },
        onProfileNavigate = { },
        onDeletePost = { },
        isLiked = false,
        isSaved = false,
        isProfile = false
    )
}

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
    onBookmarkPost: (Post) -> Unit = { },
    onUnBookmarkPost: (Post) -> Unit = { },
    onLikePost: (Post) -> Unit = { },
    onUnlikePost: (Post) -> Unit = { },
    openDeleteDialog: (Post) -> Unit = { },
    navigateToEditPostScreen: (Post) -> Unit = {},
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
                .background(color = MaterialTheme.colorScheme.onPrimary)
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
                    contentDescription = stringResource(id = R.string.post_image)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.6f)
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
                            val annotatedText = buildAnnotatedString {
                                append("By: ")
                                pushStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                                append(post.postAuthor.username)
                            }
                            Text(
                                text = annotatedText,
                                modifier = Modifier
                                    .padding(vertical = 7.dp)
                                    .clickable {
                                        onProfileNavigate(post.postAuthor.userId)
                                    },
                                fontSize = 16.sp

                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()

                    ) {
                        if (isProfile) {
                            CustomIconButton(
                                imageVector = Icons.Default.Edit,
                                onClick = {
                                    navigateToEditPostScreen(post)
                                },
                                contentDescription = stringResource(id = R.string.edit_post_description)
                            )
                            CustomIconButton(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(id = R.string.delete_post_description),
                                onClick = { openDeleteDialog(post) },
                            )
                            CustomIconButton(
                                imageVector = Icons.Filled.Share,
                                contentDescription = stringResource(id = R.string.share_description),
                                onClick = {

                                }
                            )
                        } else {
                            CustomIconButton(
                                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                onClick = {
                                    if (isLiked) {
                                        onUnlikePost(post)
                                    } else {
                                        onLikePost(post)
                                    }
                                },
                                contentDescription = if (isLiked)
                                    stringResource(id = R.string.unlike_description)
                                else
                                    stringResource(id = R.string.like_description),
                            )
                            CustomIconButton(
                                imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                onClick = {
                                    if (isSaved) {
                                        onUnBookmarkPost(post)
                                    } else {
                                        onBookmarkPost(post)
                                    }
                                },
                                contentDescription = if (isSaved)
                                    stringResource(id = R.string.un_save_description)
                                else
                                    stringResource(id = R.string.save_description),
                            )
                        }
                    }
                }
            }
        }
    }
}
