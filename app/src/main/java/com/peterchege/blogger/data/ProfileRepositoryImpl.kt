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
package com.peterchege.blogger.data

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.api.requests.FollowUser
import com.peterchege.blogger.core.api.requests.UnFollowUser
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.responses.FollowResponse
import com.peterchege.blogger.core.api.responses.responses.GetFollowersResponse
import com.peterchege.blogger.core.api.responses.responses.GetFollowingResponse
import com.peterchege.blogger.core.api.responses.responses.GetPostsByUserIdResponse
import com.peterchege.blogger.core.api.responses.responses.GetUserLikeResponse
import com.peterchege.blogger.core.api.responses.responses.ProfileResponse
import com.peterchege.blogger.core.api.responses.responses.UnfollowResponse
import com.peterchege.blogger.core.api.responses.responses.UpdateUserResponse
import com.peterchege.blogger.core.api.safeApiCall
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.UriToFile
import com.peterchege.blogger.domain.repository.ProfileRepository
import com.peterchege.blogger.presentation.screens.edit_profile.EditProfileFormState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: BloggerApi,
    @ApplicationContext private val appContext: Context,
    ):ProfileRepository {

    override suspend fun getProfile(userId: String): NetworkResult<ProfileResponse> {
        return safeApiCall{ api.getUserProfile(userId = userId) }
    }

    override suspend fun getMyProfile(): NetworkResult<ProfileResponse> {
        return safeApiCall{ api.getMyProfile() }
    }

    override suspend fun getPostsByUserId(userId: String,page:Int): NetworkResult<GetPostsByUserIdResponse> {
        return safeApiCall { api.getPostByUserId(userId = userId,page = page) }
    }

    override suspend fun followUser(followUser: FollowUser): NetworkResult<FollowResponse> {
        return safeApiCall{ api.followUser(followUser = followUser) }
    }

    override suspend fun unfollowUser(unfollowUser: UnFollowUser): NetworkResult<UnfollowResponse> {
        return safeApiCall{ api.unfollowUser(unFollowUser = unfollowUser) }
    }

    override suspend fun getFollowers(
        page: Int,
        limit: Int,
        userId: String
    ): NetworkResult<GetFollowersResponse> {
        return safeApiCall { api.getUserFollowers(page = page,userId = userId) }
    }

    override suspend fun getFollowing(
        page: Int,
        limit: Int,
        userId: String
    ): NetworkResult<GetFollowingResponse> {
        return safeApiCall { api.getUserFollowing(page = page,userId = userId) }
    }

    override suspend fun getUserLikes(userId: String): NetworkResult<GetUserLikeResponse> {
        return safeApiCall { api.getUserLikes(userId) }
    }

    override suspend fun updateUserInfo(updateUser: EditProfileFormState): NetworkResult<UpdateUserResponse> {
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (updateUser.imageUrl != null){
            val file = UriToFile(context = appContext).getImageBody(Uri.parse(updateUser.imageUrl.toString()))
            val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            builder
                .addFormDataPart("userId", updateUser.userId)
                .addFormDataPart("username", updateUser.username)
                .addFormDataPart("fullName", updateUser.fullName)
                .addFormDataPart("photo", file.name, requestFile)
        }else{
            builder
                .addFormDataPart("userId", updateUser.userId)
                .addFormDataPart("username", updateUser.username)
                .addFormDataPart("fullName", updateUser.fullName)
        }
        val requestBody: RequestBody = builder.build()

        return safeApiCall { api.updateUserInfo(requestBody) }
    }

}