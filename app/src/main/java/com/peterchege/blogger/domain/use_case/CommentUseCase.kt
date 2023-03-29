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
import com.peterchege.blogger.core.api.responses.CommentResponse
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.data.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CommentUseCase @Inject constructor(
    private val repository: CommentRepository,

    ) {
    operator fun invoke(commentBody: CommentBody): Flow<Resource<CommentResponse>> = flow {
        try {
            emit(Resource.Loading<CommentResponse>())
            val commentResponse = repository.postComment(commentBody)

            emit(Resource.Success(commentResponse))
        } catch (e: HttpException) {
            emit(Resource.Error<CommentResponse>(e.localizedMessage ?: "Your Comment was not sent"))

        } catch (e: IOException) {
            emit(Resource.Error<CommentResponse>("Could not reach server... Please check your internet connection"))

        }


    }
}