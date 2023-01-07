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
package com.peterchege.blogger.ui.dashboard.profile_screen

import android.content.SharedPreferences
import com.peterchege.blogger.api.requests.LoginUser
import com.peterchege.blogger.api.responses.LoginResponse
import com.peterchege.blogger.api.responses.ProfileResponse
import com.peterchege.blogger.ui.login.LoginRepository
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,

) {

    operator fun invoke(username:String) : Flow<Resource<ProfileResponse>> = flow {
        try {
            emit(Resource.Loading<ProfileResponse>())

            val profileResponse = repository.getProfile(username)
            if (profileResponse.success){
                emit(Resource.Success(profileResponse))
            }else{
                emit(Resource.Error<ProfileResponse>( "User could not be found"))
            }

        }catch (e: HttpException){
            emit(Resource.Error<ProfileResponse>(e.localizedMessage ?: "Server error"))

        }catch (e: IOException){
            emit(Resource.Error<ProfileResponse>("Could not reach server"))

        }


    }
}