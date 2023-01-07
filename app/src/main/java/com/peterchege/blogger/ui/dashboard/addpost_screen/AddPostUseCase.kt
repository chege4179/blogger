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
package com.peterchege.blogger.ui.dashboard.addpost_screen

import android.util.Log
import com.peterchege.blogger.api.requests.PostBody
import com.peterchege.blogger.api.responses.UploadPostResponse
import com.peterchege.blogger.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AddPostUseCase @Inject constructor(
    private val repository: AddPostRepository

) {
    operator fun invoke(postBody: PostBody) : Flow<Resource<UploadPostResponse>> = flow {
        try {
            emit(Resource.Loading<UploadPostResponse>())

            val uploadResponse = repository.uploadPost(postBody)

            emit(Resource.Success(uploadResponse))

        }catch (e: HttpException){
            emit(Resource.Error<UploadPostResponse>(e.localizedMessage ?: "An unexpected error occurred"))

        }catch (e: IOException){
            Log.e("IO ",e.localizedMessage)
            emit(Resource.Error<UploadPostResponse>("Could not reach server... Please check your internet connection"))

        }


    }
}