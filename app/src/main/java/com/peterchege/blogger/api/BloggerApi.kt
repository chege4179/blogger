/*
 * Copyright 2023 Blogger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peterchege.blogger.api

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.Delete
import com.peterchege.blogger.api.requests.*
import com.peterchege.blogger.api.responses.*
import com.peterchege.blogger.util.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @POST("/post/add")
    suspend fun postImage(
        @Body body: RequestBody
    ): UploadPostResponse

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

    companion object {
        val instance by lazy {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build()
                .create(BloggerApi::class.java)
        }
    }

}

