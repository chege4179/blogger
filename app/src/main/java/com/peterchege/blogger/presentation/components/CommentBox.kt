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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.peterchege.blogger.core.api.responses.models.CommentCount
import com.peterchege.blogger.core.api.responses.models.CommentUser
import com.peterchege.blogger.core.api.responses.models.CommentWithUser
import com.peterchege.blogger.core.api.responses.models.User
import java.util.UUID
import com.peterchege.blogger.R


@OptIn(ExperimentalFoundationApi::class)
@ExperimentalCoilApi
@Composable
fun CommentBox(
    postAuthorId: String,
    comment: CommentWithUser,
    authUser: User?,
    openDeleteCommentDialog: () -> Unit,
    setCommentToBeDeleted: (CommentWithUser) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {

                    },
                    onLongClick = {
                        if (authUser != null && authUser.userId != "") {
                            if (postAuthorId == authUser.userId) {
                                setCommentToBeDeleted(comment)
                                openDeleteCommentDialog()
                            }
                        }
                    }
                ),
        ) {
            Image(
                modifier = Modifier
                    .padding(5.dp)
                    .width(40.dp)
                    .height(40.dp)
                    .clip(CircleShape),
                painter = rememberImagePainter(
                    data = comment.user.imageUrl,
                    builder = {
                        crossfade(true)
                    },
                ),
                contentDescription = "Image of ${comment.user.fullName}"
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                val annotatedText = buildAnnotatedString {
                    pushStyle(SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 16.sp))
                    append(comment.user.username + " ")
                    pop()
                    append(comment.message)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()

                        .padding(5.dp)


                ) {
                    Text(
                        text = annotatedText,
                        textAlign = TextAlign.Start,
                        textDecoration = TextDecoration.None,
                        color = Color.Black,
                    )

                }
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomIconButton(
                    imageVector = Icons.Default.FavoriteBorder,
                    onClick = {
                        TODO("implement like comment")
                    },
                    contentDescription = stringResource(id = R.string.like_comment_description)
                )
                Text(
                    text = comment._count.commentLikes.toString()
                )
            }


        }

    }

}


@Preview
@Composable
fun CommentBoxPreview() {
    CommentBox(
        comment = CommentWithUser(
            commentId = UUID.randomUUID().toString(),
            message = "Dummy Text is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum",
            commentUserId = UUID.randomUUID().toString(),
            commentPostId = UUID.randomUUID().toString(),
            parentId = null,
            createdAt = "2023-12-02T18:55:36.935Z",
            updatedAt = "2023-12-02T18:55:36.935Z",
            _count = CommentCount(commentLikes = 23),
            user = CommentUser(
                userId = UUID.randomUUID().toString(),
                email = "peter@gmail.com",
                fullName = "peter",
                username = "peter",
                imageUrl = "https://ui-avatars.com/api/?background=719974&color=fff&name=Peter+Chege&bold=true&fontsize=0.6",
                createdAt = "2023-12-02T18:55:36.935Z",
                updatedAt = "2023-12-02T18:55:36.935Z",
                password = "2023-12-02T18:55:36.935Z",
            )
        ),
        authUser = null,
        postAuthorId = "",
        openDeleteCommentDialog = { },
        setCommentToBeDeleted = { }
    )

}