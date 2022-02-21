package com.peterchege.blogger.ui.login

import android.util.Log
import com.peterchege.blogger.api.requests.LogoutUser
import com.peterchege.blogger.api.responses.LogoutResponse
import com.peterchege.blogger.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: LoginRepository,

) {
    operator fun invoke(logoutUser: LogoutUser) : Flow<Resource<LogoutResponse>> = flow {
        try {
            emit(Resource.Loading<LogoutResponse>())
            Log.d("Logout","try error")
            val logoutResponse = repository.logoutUser(logoutUser)

            emit(Resource.Success(logoutResponse))

        }catch (e: HttpException){
            Log.d("Logout","http error")
            emit(Resource.Error<LogoutResponse>(e.localizedMessage ?: "An unexpected error occurred"))

        }catch (e: IOException){
            Log.d("Logout","io error")
            emit(Resource.Error<LogoutResponse>("Could not reach server... Please check your internet connection"))

        }


    }
}