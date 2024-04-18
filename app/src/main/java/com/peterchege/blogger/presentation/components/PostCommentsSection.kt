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
package com.peterchege.blogger.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.peterchege.blogger.core.api.responses.models.CommentWithUser
import com.peterchege.blogger.core.api.responses.models.User

fun LazyListScope.postCommentsSection(
    postAuthorId:String,
    comments: LazyPagingItems<CommentWithUser>,
    authUser: User?,
    toggleDeleteCommentDialog: () -> Unit,
    setCommentToBeDeleted:(CommentWithUser) -> Unit,
    setCommentToBeRepliedTo:(CommentWithUser?) -> Unit,
    toggleReplyCommentDialog:() -> Unit,
    addParticipants:(List<String>) -> Unit,
    likeComment:(String,String) -> Unit,

) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Comments (${comments.itemCount})",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        }
        HorizontalDivider(
            color = Color.Blue,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 0.dp)
        )
    }
    items(
        count = comments.itemCount,
        key = { it }
    ) { position ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .background(Color.Transparent)
        ) {
            comments[position]?.let {
                CommentBox(
                    comment = it,
                    authUser = authUser,
                    postAuthorId = postAuthorId,
                    openDeleteCommentDialog = toggleDeleteCommentDialog,
                    setCommentToBeDeleted = setCommentToBeDeleted,
                    setCommentToBeReplied = setCommentToBeRepliedTo,
                    toggleReplyCommentDialog = toggleReplyCommentDialog,
                    addParticipants = addParticipants,
                    likeComment = likeComment,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    item {
        if (comments.loadState.append is LoadState.Loading) {
            PagingLoader()
        }
    }
}