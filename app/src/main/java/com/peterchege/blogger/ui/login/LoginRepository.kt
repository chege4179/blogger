package com.peterchege.blogger.ui.login

import com.peterchege.blogger.api.BloggerApi
import com.peterchege.blogger.api.requests.LoginUser
import com.peterchege.blogger.api.requests.LogoutUser
import com.peterchege.blogger.api.responses.LoginResponse
import com.peterchege.blogger.api.responses.LogoutResponse

import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val api: BloggerApi,

) {
    suspend fun loginUser(loginUser: LoginUser) : LoginResponse {
        return api.loginUser(loginUser)
    }
    suspend fun logoutUser(logoutUser: LogoutUser): LogoutResponse {
        return api.logoutUser(logoutUser)
    }
}