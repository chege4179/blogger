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

import com.peterchege.blogger.core.api.responses.responses.ProfileResponse
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.Resource
import com.peterchege.blogger.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,

    ) {
    val TAG = GetProfileUseCase::class.java.simpleName

    operator fun invoke(userId: String): Flow<Resource<ProfileResponse>> = flow {
        Timber.tag(TAG).i("Userid  passed >>> $userId")
        emit(Resource.Loading<ProfileResponse>())
        val profileResponse = repository.getProfile(userId)
        when(profileResponse){
            is NetworkResult.Success -> {
                if (profileResponse.data.success) {
                    Timber.tag(TAG).i("Response success >>> ${profileResponse}")
                    emit(Resource.Success(profileResponse.data))
                } else {
                    Timber.tag(TAG).i("Response error else >>> ${profileResponse}")
                    emit(Resource.Error<ProfileResponse>("User could not be found"))
                }
            }
            is NetworkResult.Error -> {
                Timber.tag(TAG).i("Response error >>> ${profileResponse}")
                emit(Resource.Error<ProfileResponse>(profileResponse.message ?: "Server error"))
            }
            is NetworkResult.Exception -> {
                Timber.tag(TAG).i("Response exception >>> ${profileResponse}")
                emit(Resource.Error<ProfileResponse>("Could not reach server"))
            }
        }
    }
}