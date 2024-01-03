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

import com.peterchege.blogger.core.api.requests.LoginUser
import com.peterchege.blogger.core.api.requests.LogoutUser
import com.peterchege.blogger.core.api.requests.SignUpUser
import com.peterchege.blogger.core.api.responses.Following
import com.peterchege.blogger.core.api.responses.LoginResponse
import com.peterchege.blogger.core.api.responses.LogoutResponse
import com.peterchege.blogger.core.api.responses.SignUpResponse
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isUserLoggedIn :Flow<Boolean>

    suspend fun setAuthToken(token:String)
    suspend fun signUpUser(signUpUser: SignUpUser):NetworkResult <SignUpResponse>

    suspend fun loginUser(loginUser: LoginUser):NetworkResult<LoginResponse>

    suspend fun logoutUser(logoutUser: LogoutUser):NetworkResult<LogoutResponse>

    suspend fun addUserFollowing(following: Following)

    suspend fun removeUserFollowing(following: Following)

    fun getLoggedInUser(): Flow<User?>

    suspend fun setLoggedInUser(user: User)


    suspend fun unsetLoggedInUser()

}