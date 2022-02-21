package com.peterchege.blogger.ui.post_screen

import com.peterchege.blogger.api.responses.Post
import com.peterchege.blogger.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetPostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(postId:String) : Flow<Resource<Post?>> = flow {
        try {
            emit(Resource.Loading<Post?>())
            val post = repository.getPostById(postId)
            if (post == null){
                emit(Resource.Error<Post?>("This post has been deleted right now"))
            }else{
                emit(Resource.Success<Post?>(post))
            }
        }catch (e: HttpException){
            emit(Resource.Error<Post?>(e.localizedMessage ?: "An unexpected error occurred"))

        }catch (e: IOException){
            emit(Resource.Error<Post?>("Could not reach server... Please check your internet connection"))

        }


    }


}