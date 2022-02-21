package com.peterchege.blogger.api

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.Delete
import com.peterchege.blogger.api.requests.*
import com.peterchege.blogger.api.responses.*
import okhttp3.MultipartBody
import retrofit2.http.*


interface BloggerApi {

    @POST("/user/login")
    suspend fun loginUser(@Body user : LoginUser): LoginResponse
    @POST("/user/logout")
    suspend fun logoutUser(@Body user: LogoutUser): LogoutResponse

    @POST("/user/signup")
    suspend fun signUpUser(@Body user: SignUpUser): SignUpResponse

    @GET("/post/all")
    suspend fun getAllPosts():AllPostsResponse

    @POST("/post/upload")
    suspend fun uploadPost(@Body postBody: PostBody):UploadPostResponse


    @GET("/post/single/{postId}")
    suspend fun getPostById(@Path("postId") postId:String):PostResponse

    @DELETE("/post/delete/{postId}")
    suspend fun getDeletePostById(@Path("postId") postId:String):DeleteResponse

    @POST("/comment/add")
    suspend fun postComment(@Body commentbody: CommentBody):CommentResponse

    @POST("/like/add")
    suspend fun likePost(@Body likePost:LikePost):LikeResponse

    @POST("/like/remove")
    suspend fun unlikePost(@Body likePost:LikePost):LikeResponse

    @POST("/follower/follow")
    suspend fun followUser(@Body followUser:FollowUser):FollowResponse

    @POST("/follower/unfollow")
    suspend fun unfollowUser(@Body followUser:FollowUser):FollowResponse

    @GET("/user/profile/{username}")
    suspend fun getUserProfile(@Path("username") username:String):ProfileResponse

    @POST("/user/updateToken")
    suspend fun updateToken(@Body updateToken:UpdateToken):UpdateTokenResponse

    @GET("/post/search/{searchTerm}")
    suspend fun searchPost(@Path("searchTerm") searchTerm :String):SearchPostResponse

    @POST("/view/add")
    suspend fun addView(@Body viewer:Viewer):ViewResponse


}

