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
package com.peterchege.blogger.core.di

import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.room.database.BloggerDatabase
import com.peterchege.blogger.data.*
import com.peterchege.blogger.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(api: BloggerApi): AuthRepository {
        return AuthRepositoryImpl(api = api)
    }
    @Provides
    @Singleton
    fun provideCommentRepository(api: BloggerApi): CommentRepository {
        return CommentRepositoryImpl(api = api)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(api: BloggerApi): ProfileRepository {
        return ProfileRepositoryImpl(api = api)
    }

    @Provides
    @Singleton
    fun providePostRepository(
        api: BloggerApi,
        db: BloggerDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher): PostRepository {
        return PostRepositoryImpl(
            api = api,
            db = db,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideDraftRepository(db: BloggerDatabase): DraftRepository {
        return DraftRepositoryImpl(db = db)
    }
}