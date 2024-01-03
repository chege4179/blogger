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

import com.peterchege.blogger.core.util.NetworkResult
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

suspend fun <T : Any> safeApiCall(
    execute: suspend () -> Response<T>
): NetworkResult<T> {
    val TAG = "Network Error"
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        } else {
            NetworkResult.Error(code = response.code(), message = response.message())
        }
    } catch (e: HttpException) {
        Timber.tag(TAG).i("The HTTP error causing this is -----> $e")
        NetworkResult.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        Timber.tag(TAG).i("The HTTP error causing this is -----> $e")
        NetworkResult.Exception(e)
    }
}