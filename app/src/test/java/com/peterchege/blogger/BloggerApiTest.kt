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
package com.peterchege.blogger

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.api.safeApiCall
import com.peterchege.blogger.core.util.NetworkResult
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import java.io.InputStream
import java.net.HttpURLConnection

@RunWith(RobolectricTestRunner::class)
class BloggerApiTest {

    private var context: Context? = null
    private var mockWebServer = MockWebServer()
    private lateinit var bloggerApi: BloggerApi


    @Before
    fun setup() {
        mockWebServer.start()

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        context = InstrumentationRegistry.getInstrumentation().targetContext


        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val contentType = "application/json".toMediaType()
        val converterFactory = Json { ignoreUnknownKeys = true }.asConverterFactory(contentType)

        bloggerApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(BloggerApi::class.java)


    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun when_given_a_proper_response_network_success_result_is_returned(): Unit = runBlocking {
        val jsonStream: InputStream = context!!.resources.assets.open("all_posts_success.json")
        val jsonBytes: ByteArray = jsonStream.readBytes()
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(String(jsonBytes))
        mockWebServer.enqueue(response)

        val data = safeApiCall { bloggerApi.getAllPosts() }
        assert(data is NetworkResult.Success)


    }

    @Test
    fun when_given_an_error_response_network_error_result_is_returned(): Unit = runBlocking {
        val jsonStream: InputStream = context!!.resources.assets.open("all_posts_error.json")
        val jsonBytes: ByteArray = jsonStream.readBytes()
        val response = MockResponse()
            .setResponseCode(400)
            .setBody(String(jsonBytes))
        mockWebServer.enqueue(response)

        val data = safeApiCall { bloggerApi.getAllPosts() }
        assert(data is NetworkResult.Error)


    }


}