package com.peterchege.blogger.ui.signup

import com.peterchege.blogger.api.BloggerApi
import com.peterchege.blogger.api.requests.SignUpUser
import com.peterchege.blogger.api.responses.SignUpResponse
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val api: BloggerApi
) {
    suspend fun signUpUser(signUpUser: SignUpUser) : SignUpResponse {
        return api.signUpUser(signUpUser)
    }


}