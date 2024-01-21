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
package com.peterchege.blogger.data

import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.api.requests.CommentBody
import com.peterchege.blogger.core.api.requests.DeleteCommentBody
import com.peterchege.blogger.core.api.requests.ReplyCommentBody
import com.peterchege.blogger.core.api.responses.responses.AddCommentResponse
import com.peterchege.blogger.core.api.responses.responses.DeleteCommentResponse
import com.peterchege.blogger.core.api.responses.responses.GetCommentsResponse
import com.peterchege.blogger.core.api.safeApiCall
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.domain.repository.CommentRepository
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val api: BloggerApi
) : CommentRepository {
    override suspend fun postComment(commentBody: CommentBody): NetworkResult<AddCommentResponse> {
        return safeApiCall { api.postComment(commentBody = commentBody) }
    }

    override suspend fun getAllComments(
        page: Int,
        limit: Int,
        postId: String
    ): NetworkResult<GetCommentsResponse> {
        return safeApiCall { api.getAllComments(page = page, limit = limit, postId = postId) }
    }

    override suspend fun replyToComment(commentBody: ReplyCommentBody): NetworkResult<AddCommentResponse> {
        return safeApiCall { api.replyToComment(commentBody = commentBody) }
    }

    override suspend fun deleteComment(deleteCommentBody: DeleteCommentBody): NetworkResult<DeleteCommentResponse> {
        return safeApiCall { api.removeComment(commentBody = deleteCommentBody) }
    }

}