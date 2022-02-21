package com.peterchege.blogger.ui.login

import android.content.SharedPreferences
import com.peterchege.blogger.api.requests.LoginUser
import com.peterchege.blogger.api.responses.LoginResponse
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository,
    private val sharedPreferences: SharedPreferences

) {

    operator fun invoke(loginUser: LoginUser) : Flow<Resource<LoginResponse>> = flow {
        try {
            emit(Resource.Loading<LoginResponse>())

            val loginResponse = repository.loginUser(loginUser)
            val userInfoEditor = sharedPreferences.edit()
            emit(Resource.Success(loginResponse))
            userInfoEditor.apply{
                putString(Constants.LOGIN_USERNAME,loginResponse.user?.username)
                putString(Constants.LOGIN_PASSWORD, loginResponse.user?.password)
                putString(Constants.LOGIN_FULLNAME, loginResponse.user?.fullname)
                putString(Constants.LOGIN_IMAGEURL, loginResponse.user?.imageUrl)
                putString(Constants.LOGIN_EMAIL, loginResponse.user?.email)
                putString(Constants.LOGIN_ID, loginResponse.user?._id)
                apply()
            }
        }catch (e: HttpException){
            emit(Resource.Error<LoginResponse>(e.localizedMessage ?: "Log In failed......Server error"))

        }catch (e: IOException){
            emit(Resource.Error<LoginResponse>("Could not reach server... Please check your internet connection"))

        }


    }
}