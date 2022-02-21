package com.peterchege.blogger.ui.signup

import com.peterchege.blogger.api.requests.SignUpUser
import com.peterchege.blogger.api.responses.SignUpResponse
import com.peterchege.blogger.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: SignUpRepository
) {
    operator fun invoke(signUpUser: SignUpUser) : Flow<Resource<SignUpResponse>> = flow {
        try {
            emit(Resource.Loading<SignUpResponse>())
            val signUpResponse = repository.signUpUser(signUpUser)
            emit(Resource.Success(signUpResponse))

        }catch (e: HttpException){
            emit(Resource.Error<SignUpResponse>(e.localizedMessage ?: "An unexpected error occurred"))

        }catch (e: IOException){
            emit(Resource.Error<SignUpResponse>("Could not reach server... Please check your internet connection"))

        }


    }
}