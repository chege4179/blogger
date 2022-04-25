package com.peterchege.blogger.ui.dashboard.home_screen.feed_screen

import com.peterchege.blogger.api.responses.Post
import com.peterchege.blogger.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetFeedUseCase @Inject constructor(
    private val repository: FeedRepository
) {
    operator fun invoke() : Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading<List<Post>>())
            val posts = repository.getFeedPosts()
            emit(Resource.Success(posts))
        }catch (e: HttpException){
            emit(Resource.Error<List<Post>>(e.localizedMessage ?: "An unexpected error occurred"))

        }catch (e: IOException){
            emit(Resource.Error<List<Post>>("Could not reach the internet...Please check your internet connection"))
        }


    }
}