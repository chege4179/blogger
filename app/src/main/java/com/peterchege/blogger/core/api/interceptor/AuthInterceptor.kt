package com.peterchege.blogger.core.api.interceptor

import com.peterchege.blogger.core.datastore.preferences.DefaultAuthTokenProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor


class AuthInterceptor(
    private val authTokenProvider:DefaultAuthTokenProvider
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        val authToken = runBlocking { authTokenProvider.authToken.first() }
        request = request.newBuilder()
            .header(name = "Authorization", value = "Bearer $authToken")
            .build()
        return chain.proceed(request)
    }
}