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
        try {
            emit(Resource.Loading<LogoutResponse>())
            Log.d("Logout", "try error")
            val logoutResponse = repository.logoutUser(logoutUser)

            emit(Resource.Success(logoutResponse))

        } catch (e: HttpException) {
            Log.d("Logout", "http error")
            emit(
                Resource.Error<LogoutResponse>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )

        } catch (e: IOException) {
            Log.d("Logout", "io error")
            emit(Resource.Error<LogoutResponse>("Could not reach server... Please check your internet connection"))

        }


    }
}