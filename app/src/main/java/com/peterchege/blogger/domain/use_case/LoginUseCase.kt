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

import android.content.SharedPreferences
import com.peterchege.blogger.core.api.requests.LoginUser
import com.peterchege.blogger.core.api.responses.LoginResponse
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val sharedPreferences: SharedPreferences

) {

    operator fun invoke(loginUser: LoginUser): Flow<Resource<LoginResponse>> = flow {
        try {
            emit(Resource.Loading<LoginResponse>())

            val loginResponse = repository.loginUser(loginUser)
            val userInfoEditor = sharedPreferences.edit()
            emit(Resource.Success(loginResponse))
            userInfoEditor.apply {
                putString(Constants.LOGIN_USERNAME, loginResponse.user?.username)
                putString(Constants.LOGIN_PASSWORD, loginResponse.user?.password)
                putString(Constants.LOGIN_FULLNAME, loginResponse.user?.fullname)
                putString(Constants.LOGIN_IMAGEURL, loginResponse.user?.imageUrl)
                putString(Constants.LOGIN_EMAIL, loginResponse.user?.email)
                putString(Constants.LOGIN_ID, loginResponse.user?._id)
                apply()
            }
        } catch (e: HttpException) {
            emit(
                Resource.Error<LoginResponse>(
                    e.localizedMessage ?: "Log In failed......Server error"
                )
            )

        } catch (e: IOException) {
            emit(Resource.Error<LoginResponse>("Could not reach server... Please check your internet connection"))

        }


    }
}