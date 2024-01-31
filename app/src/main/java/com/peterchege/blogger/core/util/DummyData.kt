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
package com.peterchege.blogger.core.util

import com.peterchege.blogger.core.api.requests.CommentBody
import com.peterchege.blogger.core.api.requests.DeleteCommentBody
import com.peterchege.blogger.core.api.requests.LikeCommentBody
import com.peterchege.blogger.core.api.requests.ReplyCommentBody
import com.peterchege.blogger.core.api.responses.models.CommentCount
import com.peterchege.blogger.core.api.responses.models.CommentUser
import com.peterchege.blogger.core.api.responses.models.CommentWithUser
import com.peterchege.blogger.core.api.responses.responses.AddCommentResponse
import com.peterchege.blogger.core.api.responses.responses.DeleteCommentResponse
import com.peterchege.blogger.core.api.responses.responses.GetCommentsResponse
import com.peterchege.blogger.core.api.responses.responses.LikeCommentResponse
import com.peterchege.blogger.domain.repository.CommentRepository
import java.util.UUID

class DummyCommentsRepository : CommentRepository {
    override suspend fun postComment(commentBody: CommentBody): NetworkResult<AddCommentResponse> {
        return NetworkResult.Error(code = 200,message = null)
    }

    override suspend fun likeComment(likeCommentBody: LikeCommentBody): NetworkResult<LikeCommentResponse> {
        return NetworkResult.Error(code = 200,message = null)
    }

    override suspend fun getAllComments(
        page: Int,
        limit: Int,
        postId: String
    ): NetworkResult<GetCommentsResponse> {
        return NetworkResult.Success(
            data = GetCommentsResponse(
                msg = "Success",
                success = true,
                comments = dummyData,
                nextPage = 2
            )
        )
    }

    override suspend fun replyToComment(commentBody: ReplyCommentBody): NetworkResult<AddCommentResponse> {
        return NetworkResult.Error(code = 200,message = null)
    }

    override suspend fun deleteComment(deleteCommentBody: DeleteCommentBody): NetworkResult<DeleteCommentResponse> {
        return NetworkResult.Error(code = 200,message = null)
    }


    private val dummyData = (1..100).map {
        CommentWithUser(
            commentId = UUID.randomUUID().toString() + it,
            message = "is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum $it",
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
        )
    }

}