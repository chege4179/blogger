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
import com.peterchege.blogger.core.api.requests.LoginUser
import com.peterchege.blogger.core.api.requests.LogoutUser
import com.peterchege.blogger.core.api.requests.SignUpUser
import com.peterchege.blogger.core.api.responses.LoginResponse
import com.peterchege.blogger.core.api.responses.LogoutResponse
import com.peterchege.blogger.core.api.responses.SignUpResponse
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.api.safeApiCall
import com.peterchege.blogger.core.datastore.repository.UserDataStoreRepository
import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl  @Inject constructor(
    private val api: BloggerApi,
    private val userDataStoreRepository: UserDataStoreRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
): AuthRepository {

    override suspend fun signUpUser(signUpUser: SignUpUser): NetworkResult<SignUpResponse> {
        return safeApiCall { api.signUpUser(signUpUser) }
    }

    override suspend fun loginUser(loginUser: LoginUser): NetworkResult<LoginResponse> {
        return safeApiCall { api.loginUser(loginUser) }
    }

    override suspend fun logoutUser(logoutUser: LogoutUser): NetworkResult<LogoutResponse> {
        return safeApiCall { api.logoutUser(logoutUser) }
    }

    override fun getLoggedInUser(): Flow<User?> {
        return userDataStoreRepository.getLoggedInUser().flowOn(ioDispatcher)
    }

    override suspend fun setLoggedInUser(user: User) = withContext(context = ioDispatcher) {
        return@withContext userDataStoreRepository.setLoggedInUser(user = user)
    }

    override suspend fun unsetLoggedInUser() = withContext(context = ioDispatcher) {
        return@withContext userDataStoreRepository.unsetLoggedInUser()
    }


}