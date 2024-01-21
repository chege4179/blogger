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

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.peterchege.blogger.core.api.responses.models.CommentWithUser
import com.peterchege.blogger.core.util.DummyCommentsRepository
import com.peterchege.blogger.domain.paging.CommentsPagingSource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagingApi::class)
@Preview
@Composable
fun CommentsBottomSheetPreview() {
    CommentsBottomSheet(
        comments = Pager(
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
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = true,
            isFocusable = true,
            securePolicy = SecureFlagPolicy.SecureOn
        )
    ) {

    }
}
