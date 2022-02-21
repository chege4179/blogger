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
            emit(Resource.Error<ProfileResponse>("Could not reach server... Please check your internet connection"))

        }


    }
}