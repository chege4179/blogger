package com.peterchege.blogger.ui.dashboard.addpost_screen

import android.util.Log
import com.peterchege.blogger.api.requests.PostBody
import com.peterchege.blogger.api.responses.UploadPostResponse
import com.peterchege.blogger.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AddPostUseCase @Inject constructor(
    private val repository: AddPostRepository

) {
    operator fun invoke(postBody: PostBody) : Flow<Resource<UploadPostResponse>> = flow {
        try {
            emit(Resource.Loading<UploadPostResponse>())

            val uploadResponse = repository.uploadPost(postBody)

            emit(Resource.Success(uploadResponse))

        }catch (e: HttpException){
            emit(Resource.Error<UploadPostResponse>(e.localizedMessage ?: "An unexpected error occurred"))

        }catch (e: IOException){
            Log.e("IO ",e.localizedMessage)
            emit(Resource.Error<UploadPostResponse>("Could not reach server... Please check your internet connection"))

        }


    }
}