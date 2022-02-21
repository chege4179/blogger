package com.peterchege.blogger.ui.post_screen

import com.peterchege.blogger.api.requests.CommentBody
import com.peterchege.blogger.api.responses.CommentResponse
import com.peterchege.blogger.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PostCommentUseCase @Inject constructor(
    private val repository: CommentRepository,
) {
    operator fun invoke(commentBody: CommentBody) : Flow<Resource<CommentResponse>> = flow {
        try {
            emit(Resource.Loading<CommentResponse>())
            val response = repository.postComment(commentBody)
            emit(Resource.Success(response))
        }catch (e: HttpException){
            emit(Resource.Error<CommentResponse>(e.localizedMessage ?: "An unexpected error occurred"))

        }catch (e: IOException){
            emit(Resource.Error<CommentResponse>("Could not reach server... Please check your internet connection"))

        }


    }
}