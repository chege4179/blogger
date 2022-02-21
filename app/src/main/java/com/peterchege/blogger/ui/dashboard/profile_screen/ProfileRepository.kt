package com.peterchege.blogger.ui.dashboard.profile_screen

import com.peterchege.blogger.api.BloggerApi
import com.peterchege.blogger.api.requests.LoginUser
import com.peterchege.blogger.api.requests.LogoutUser
import com.peterchege.blogger.api.responses.LoginResponse
import com.peterchege.blogger.api.responses.LogoutResponse
import com.peterchege.blogger.api.responses.ProfileResponse
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val api: BloggerApi,

    ) {
    suspend fun getProfile(username:String) : ProfileResponse {
        return api.getUserProfile(username = username)
    }

}