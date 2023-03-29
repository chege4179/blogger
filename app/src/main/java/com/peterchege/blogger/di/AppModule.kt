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
package com.peterchege.blogger.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.peterchege.blogger.api.BloggerApi
import com.peterchege.blogger.room.database.BloggerDatabase
import com.peterchege.blogger.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                ChuckerInterceptor.Builder(context = context)
                    .collector(ChuckerCollector(context = context))
                    .maxContentLength(length = 250000L)
                    .redactHeaders(headerNames = emptySet())
                    .alwaysReadResponseBody(enable = false)
                    .build()
            )
            .build()
    }
    @Provides
    @Singleton
    fun provideUserApi(client: OkHttpClient):BloggerApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .build()
            .create(BloggerApi::class.java)
    }
    @Provides
    @Singleton
    fun provideSharedPreference(app:Application):SharedPreferences{
        return app.getSharedPreferences("user",Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideBloggerDatabase(app: Application): BloggerDatabase {
        return Room.databaseBuilder(
            app,
            BloggerDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }
}