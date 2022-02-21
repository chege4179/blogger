package com.peterchege.blogger.ui.post_screen

import com.peterchege.blogger.api.BloggerApi
import com.peterchege.blogger.api.requests.FollowUser
import com.peterchege.blogger.api.requests.LikePost
import com.peterchege.blogger.api.requests.Viewer
import com.peterchege.blogger.api.responses.*

import com.peterchege.blogger.room.database.BloggerDatabase
import com.peterchege.blogger.room.entities.PostRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepository @Inject constructor (
    private val api: BloggerApi,
    private val db: BloggerDatabase
) {
    suspend fun getPostById(postId:String) : Post? {
        return api.getPostById(postId).post
    }
    suspend fun deletePostFromApi(postId: String): DeleteResponse {
        return api.getDeletePostById(postId)
    }
    suspend fun addView(viewer: Viewer):ViewResponse{
        return api.addView(viewer = viewer)
    }

    suspend fun likePost(likePost: LikePost):LikeResponse{
        return api.likePost(likePost = likePost)
    }
    suspend fun unlikePost(likePost: LikePost):LikeResponse{
        return api.unlikePost(likePost = likePost)
    }

    suspend fun followUser(followUser: FollowUser):FollowResponse{
        return api.followUser(followUser = followUser)
    }
    suspend fun unfollowUser(followUser: FollowUser):FollowResponse{
        return api.unfollowUser(followUser = followUser)
    }

    suspend fun insertPost(post:PostRecord){
        return db.postDao.insertPost(post)

    }
    suspend fun deleteAllPosts(){
        return db.postDao.deleteAllPosts()

    }
    suspend fun deletePostById(id:String){
        return db.postDao.deletePostById(id)
    }
    suspend fun getPostFromRoom(postId:String):PostRecord?{
        return db.postDao.getPostById(postId)
    }
    fun getAllPostsFromRoom(): Flow<List<PostRecord>> {
        return db.postDao.getPosts()
    }
}