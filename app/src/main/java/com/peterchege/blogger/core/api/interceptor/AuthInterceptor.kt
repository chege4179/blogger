/*
 * Copyright 2024 Blogger
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