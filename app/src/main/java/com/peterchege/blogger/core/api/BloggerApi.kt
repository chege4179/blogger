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
package com.peterchege.blogger.core.api


import com.peterchege.blogger.core.api.requests.*
import com.peterchege.blogger.core.api.responses.*
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.api.responses.responses.AllPostsResponse
import com.peterchege.blogger.core.api.responses.responses.AddCommentResponse
import com.peterchege.blogger.core.api.responses.responses.DeleteCommentResponse
import com.peterchege.blogger.core.api.responses.responses.DeleteResponse
import com.peterchege.blogger.core.api.responses.responses.FollowResponse
import com.peterchege.blogger.core.api.responses.responses.GetCommentsResponse
import com.peterchege.blogger.core.api.responses.responses.GetFollowersResponse
import com.peterchege.blogger.core.api.responses.responses.GetFollowingResponse
import com.peterchege.blogger.core.api.responses.responses.GetPostLikesResponse
import com.peterchege.blogger.core.api.responses.responses.GetPostsByUserIdResponse
import com.peterchege.blogger.core.api.responses.responses.GetUserLikeResponse
import com.peterchege.blogger.core.api.responses.responses.GetUserNotificationsResponse
import com.peterchege.blogger.core.api.responses.responses.LikeCommentResponse
import com.peterchege.blogger.core.api.responses.responses.LikeResponse
import com.peterchege.blogger.core.api.responses.responses.LoginResponse
import com.peterchege.blogger.core.api.responses.responses.LogoutResponse
import com.peterchege.blogger.core.api.responses.responses.OtpTriggerResponse
import com.peterchege.blogger.core.api.responses.responses.OtpValidateResponse
import com.peterchege.blogger.core.api.responses.responses.PostResponse
import com.peterchege.blogger.core.api.responses.responses.ProfileResponse
import com.peterchege.blogger.core.api.responses.responses.ResetPasswordResponse
import com.peterchege.blogger.core.api.responses.responses.SearchPostResponse
import com.peterchege.blogger.core.api.responses.responses.SearchUserResponse
import com.peterchege.blogger.core.api.responses.responses.SignUpResponse
import com.peterchege.blogger.core.api.responses.responses.UnLikeResponse
import com.peterchege.blogger.core.api.responses.responses.UnfollowResponse
import com.peterchege.blogger.core.api.responses.responses.UpdatePostResponse
import com.peterchege.blogger.core.api.responses.responses.UpdateTokenResponse
import com.peterchege.blogger.core.api.responses.responses.UpdateUserResponse
import com.peterchege.blogger.core.api.responses.responses.UploadPostResponse
import com.peterchege.blogger.core.api.responses.responses.ViewResponse
import okhttp3.RequestBody
import retrofit2.Response

import retrofit2.http.*


interface BloggerApi {

    @POST("/auth/login")
    suspend fun loginUser(@Body user: LoginUser): Response<LoginResponse>

    @POST("/auth/logout")
    suspend fun logoutUser(@Body user: LogoutUser): Response<LogoutResponse>

    @POST("/auth/signup")
    suspend fun signUpUser(@Body user: SignUpUser): Response<SignUpResponse>

    @GET("/post/all")
    suspend fun getAllPosts(): Response<AllPostsResponse>

    @POST("/post/create")
    suspend fun createPost(
        @Body body: RequestBody
    ): Response<UploadPostResponse>



    @GET("/post/single/{postId}")
    suspend fun getPostById(@Path("postId") postId: String): Response<PostResponse>

    @DELETE("/post/delete/{postId}")
    suspend fun getDeletePostById(@Path("postId") postId: String): Response<DeleteResponse>

    @POST("/like/like")
    suspend fun likePost(@Body likePost: LikePost): Response<LikeResponse>

    @POST("/like/unlike")
    suspend fun unlikePost(
        @Body likePost: LikePost
    ): Response<UnLikeResponse>

    @POST("/follower/followUser")
    suspend fun followUser(
        @Body followUser: FollowUser
    ): Response<FollowResponse>

    @POST("/follower/unfollowUser")
    suspend fun unfollowUser(
        @Body unFollowUser: UnFollowUser
    ): Response<UnfollowResponse>

    @GET("/user/single/{userId}")
    suspend fun getUserProfile(@Path("userId") userId: String): Response<ProfileResponse>

    @PUT("/auth/updateDeviceToken")
    suspend fun updateToken(@Body updateToken: UpdateToken): Response<UpdateTokenResponse>

    @GET("/post/search/{searchTerm}")
    suspend fun searchPost(@Path("searchTerm") searchTerm: String): Response<SearchPostResponse>

    @GET("/user/search/{searchTerm}")
    suspend fun searchUsers(@Path("searchTerm") searchTerm: String): Response<SearchUserResponse>

    @POST("/view/add")
    suspend fun addView(@Body viewer: Viewer): Response<ViewResponse>

    @GET("/follower/getFollowers/{userId}")
    suspend fun getUserFollowers(
        @Path("userId") userId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 1000,
    ): Response<GetFollowersResponse>

    @GET("/follower/getFollowing/{userId}")
    suspend fun getUserFollowing(
        @Path("userId") userId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 1000,

        ): Response<GetFollowingResponse>

    @GET("/post/likes/{postId}")
    suspend fun getPostLikes(
        @Path("postId") postId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ): Response<GetPostLikesResponse>

    @GET("/user/likes/{userId}")
    suspend fun getUserLikes(
        @Path("userId") userId: String
    ): Response<GetUserLikeResponse>

    @GET("/post/user/{userId}")
    suspend fun getPostByUserId(
        @Path("userId") userId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ): Response<GetPostsByUserIdResponse>

    @POST("/comment/add")
    suspend fun postComment(
        @Body commentBody: CommentBody
    ): Response<AddCommentResponse>

    @POST("/comment/reply")
    suspend fun replyToComment(
        @Body commentBody: ReplyCommentBody
    ): Response<AddCommentResponse>

    @POST("/comment/remove")
    suspend fun removeComment(
        @Body commentBody: DeleteCommentBody
    ): Response<DeleteCommentResponse>


    @GET("/comment/all/{postId}")
    suspend fun getAllComments(
        @Path("postId") postId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ): Response<GetCommentsResponse>

    @PUT("/post/update")
    suspend fun updatePost(
        @Body updatePost: UpdatePost,
    ):Response<UpdatePostResponse>

    @GET("/notification/all/{userId}")
    suspend fun getUserNotifications(
        @Path("userId") userId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ):Response<GetUserNotificationsResponse>

    @POST("/comment/like")
    suspend fun likeComment(
        @Body likeCommentBody: LikeCommentBody,
    ):Response<LikeCommentResponse>


    @POST("/auth/triggerVerifyEmailOtp")
    suspend fun triggerVerifyEmailOtp(
        @Body body: OtpTriggerBody
    ):Response<OtpTriggerResponse>


    @POST("/auth/triggerPasswordResetOtp")
    suspend fun triggerPasswordResetOtp(
        @Body body: OtpTriggerBody
    ):Response<OtpTriggerResponse>


    @POST("/auth/validatePasswordResetOtp")
    suspend fun validatePasswordResetOtp(
        @Body body: ValidateOtpBody
    ):Response<OtpValidateResponse>


    @POST("/auth/validateVerifyEmailOtp")
    suspend fun validateVerifyEmailOtp(
        @Body body: ValidateOtpBody
    ):Response<OtpValidateResponse>


    @POST("/auth/resetPassword")
    suspend fun resetPassword(
        @Body body: ResetPasswordBody
    ):Response<ResetPasswordResponse>


    @PUT("/user/update")
    suspend fun updateUserInfo(
        @Body body: RequestBody
    ): Response<UpdateUserResponse>




}

