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
package com.peterchege.blogger.presentation.bottomsheets

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.peterchege.blogger.core.api.responses.models.CommentWithUser
import com.peterchege.blogger.core.util.DummyCommentsRepository
import com.peterchege.blogger.domain.paging.CommentsPagingSource
import com.peterchege.blogger.presentation.components.CommentBox
import com.peterchege.blogger.presentation.components.PagingLoader

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagingApi::class)
@Preview
@Composable
fun CommentsBottomSheetPreview() {
    CommentsBottomSheet(
        comments =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CommentsPagingSource(
                    commentRepository = DummyCommentsRepository(),
                    postId = ""
                )

            }
        ).flow.collectAsLazyPagingItems(),
        commentsBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    commentsBottomSheetState: SheetState,
    comments: LazyPagingItems<CommentWithUser>,
) {
    ModalBottomSheet(
        sheetState = commentsBottomSheetState,
        onDismissRequest = { /*TODO*/ },
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
        ) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(
                        count = comments.itemCount,
                        key = { it }
                    ) { position ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                        ) {
                            comments[position]?.let {
                                CommentBox(comment = it)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }

                    item {
                        if (comments.loadState.prepend is LoadState.Loading) {
                            PagingLoader()
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)

                ) {
                    val comment = remember {
                        mutableStateOf("some text ")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxSize(),
                            value = comment.value,
                            onValueChange = {
                                comment.value = it
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {

                                    }
                                ) {
                                    Icon(
                                        modifier = Modifier.size(30.dp),
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Send"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
