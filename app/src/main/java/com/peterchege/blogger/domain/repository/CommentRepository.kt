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
package com.peterchege.blogger.domain.repository

import com.peterchege.blogger.core.api.requests.CommentBody
import com.peterchege.blogger.core.api.requests.DeleteCommentBody
import com.peterchege.blogger.core.api.requests.LikeCommentBody
import com.peterchege.blogger.core.api.requests.ReplyCommentBody
import com.peterchege.blogger.core.api.responses.responses.AddCommentResponse
import com.peterchege.blogger.core.api.responses.responses.DeleteCommentResponse
import com.peterchege.blogger.core.api.responses.responses.GetCommentsResponse
import com.peterchege.blogger.core.api.responses.responses.LikeCommentResponse
import com.peterchege.blogger.core.util.NetworkResult

interface CommentRepository {

    suspend fun postComment(commentBody: CommentBody):NetworkResult<AddCommentResponse>

    suspend fun getAllComments(page:Int,limit:Int,postId:String):NetworkResult<GetCommentsResponse>

    suspend fun replyToComment(commentBody: ReplyCommentBody):NetworkResult<AddCommentResponse>

    suspend fun deleteComment(deleteCommentBody:DeleteCommentBody):NetworkResult<DeleteCommentResponse>


    suspend fun likeComment(likeCommentBody: LikeCommentBody):NetworkResult<LikeCommentResponse>

}