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
package com.peterchege.blogger.domain.use_case

import com.peterchege.blogger.core.api.requests.CommentBody
import com.peterchege.blogger.core.api.responses.responses.AddCommentResponse
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommentUseCase @Inject constructor(
    private val repository: CommentRepository,

    ) {
    operator fun invoke(commentBody: CommentBody): Flow<Resource<AddCommentResponse>> = flow {
        emit(Resource.Loading())
        val commentResponse = repository.postComment(commentBody)
        when(commentResponse){
            is NetworkResult.Success -> {
                emit(Resource.Success(commentResponse.data))
            }
            is NetworkResult.Exception -> {
                emit(Resource.Error<AddCommentResponse>(commentResponse.e.message ?: "Your Comment was not sent"))
            }
            is NetworkResult.Error -> {
                emit(Resource.Error<AddCommentResponse>("Could not reach server... Please check your internet connection"))
            }
        }
    }
}