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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    setCommentToBeReplied:(CommentWithUser?) -> Unit,
    toggleReplyCommentDialog:() -> Unit,
    addParticipants:(List<String>) -> Unit,
    likeComment:(String,String) -> Unit,
) {
    var isRepliesOpen by remember { mutableStateOf(false) }
    Column {
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
                        append(comment.message )
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
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = "Reply",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        textDecoration = TextDecoration.None,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.clickable {
                            setCommentToBeReplied(comment)
                            toggleReplyCommentDialog()
                            if (authUser != null){
                                addParticipants(listOf(authUser.userId,comment.commentUserId))
                            }

                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .width(50.dp)
                                .padding(horizontal = 5.dp)
                        )
                        Text(
                            text = if (isRepliesOpen)
                                "Hide Replies"
                            else
                                "Show ${comment.children.size ?: 0} replies",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start,
                            textDecoration = TextDecoration.None,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.clickable {
                                if(comment.children.isNotEmpty()){
                                    isRepliesOpen = !isRepliesOpen
                                }
                            }
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
                            authUser?.let {
                                if (it.userId != ""){
                                    likeComment(comment.commentId,it.userId)
                                }
                            }

                        },
                        contentDescription = stringResource(id = R.string.like_comment_description)
                    )
                    Text(
                        text = comment.count.commentLikes.toString()
                    )
                }


            }

        }
        AnimatedVisibility(visible = isRepliesOpen) {
            Row {
                Spacer(modifier = Modifier.width(50.dp))
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.fillMaxWidth()
                ){
                    comment.children?.forEach {
                        ReplyCommentBox(
                            postAuthorId = postAuthorId,
                            comment = it,
                            authUser = authUser,
                            openDeleteCommentDialog = { /*TODO*/ },
                            setCommentToBeDeleted = { }
                        )
                    }
                }
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
            count = CommentCount(commentLikes = 23),
            user = CommentUser(
                userId = UUID.randomUUID().toString(),
                email = "peter@gmail.com",
                fullName = "peter",
                username = "peter",
                imageUrl = "https://ui-avatars.com/api/?background=719974&color=fff&name=Peter+Chege&bold=true&fontsize=0.6",
                createdAt = "2023-12-02T18:55:36.935Z",
                updatedAt = "2023-12-02T18:55:36.935Z",
                password = "2023-12-02T18:55:36.935Z",
            ),
            children = emptyList()
        ),
        authUser = null,
        postAuthorId = "",
        openDeleteCommentDialog = { },
        setCommentToBeDeleted = { },
        setCommentToBeReplied = { },
        addParticipants = {},
        toggleReplyCommentDialog = {  },
        likeComment = { _,_ ->  }
    )

}