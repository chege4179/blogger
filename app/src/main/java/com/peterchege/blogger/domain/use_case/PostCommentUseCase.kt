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
import com.peterchege.blogger.core.api.responses.responses.CommentResponse
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.data.CommentRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostCommentUseCase @Inject constructor(
    private val repository: CommentRepositoryImpl,
) {
    operator fun invoke(commentBody: CommentBody): Flow<Resource<CommentResponse>> = flow {
        emit(Resource.Loading<CommentResponse>())
        val response = repository.postComment(commentBody)
        when(response){
            is NetworkResult.Success -> {
                emit(Resource.Success(response.data))
            }
            is NetworkResult.Error -> {
                emit(
                    Resource.Error<CommentResponse>(
                        response.message ?: "An unexpected error occurred"
                    )
                )
            }
            is NetworkResult.Exception -> {
                emit(Resource.Error<CommentResponse>("Could not reach server... Please check your internet connection"))
            }
        }
    }
}