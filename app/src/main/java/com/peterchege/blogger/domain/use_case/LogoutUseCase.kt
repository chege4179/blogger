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

import android.util.Log
import com.peterchege.blogger.core.api.requests.LogoutUser
import com.peterchege.blogger.core.api.responses.LogoutResponse
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository

) {
    operator fun invoke(logoutUser: LogoutUser): Flow<Resource<LogoutResponse>> = flow {
        emit(Resource.Loading<LogoutResponse>())
        val logoutResponse = repository.logoutUser(logoutUser)
        when (logoutResponse) {
            is NetworkResult.Exception -> {
                emit(Resource.Error<LogoutResponse>("Could not reach server... Please check your internet connection"))
            }

            is NetworkResult.Error -> {
                emit(
                    Resource.Error<LogoutResponse>(
                        logoutResponse.message ?: "An unexpected error occurred"
                    )
                )
            }

            is NetworkResult.Success -> {
                emit(Resource.Success(logoutResponse.data))
            }

        }
    }
}