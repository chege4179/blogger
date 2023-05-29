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

import android.content.Context
import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.datastore.repository.UserDataStoreRepository
import com.peterchege.blogger.core.room.database.BloggerDatabase
import com.peterchege.blogger.data.*
import com.peterchege.blogger.data.local.posts.cached_posts.CachedPostsDataSource
import com.peterchege.blogger.data.local.posts.cached_posts.CachedPostsDataSourceImpl
import com.peterchege.blogger.data.local.posts.saved_posts.SavedPostsDataSource
import com.peterchege.blogger.data.local.posts.saved_posts.SavedPostsDataSourceImpl
import com.peterchege.blogger.data.remote.posts.RemotePostsDataSource
import com.peterchege.blogger.data.remote.posts.RemotePostsDataSourceImpl
import com.peterchege.blogger.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: BloggerApi,
        userDataStoreRepository: UserDataStoreRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): AuthRepository {
        return AuthRepositoryImpl(
            api = api,
            userDataStoreRepository = userDataStoreRepository,
            ioDispatcher = ioDispatcher,
        )
    }

    @Provides
    @Singleton
    fun provideCommentRepository(api: BloggerApi): CommentRepository {
        return CommentRepositoryImpl(api = api)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        api: BloggerApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            api = api,
            ioDispatcher = ioDispatcher,
        )
    }

    @Provides
    @Singleton
    fun provideRemotePostsDataSource(
        api: BloggerApi,
        db: BloggerDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): RemotePostsDataSource {
        return RemotePostsDataSourceImpl(
            api = api,
            ioDispatcher = ioDispatcher
        )
    }


    @Provides
    @Singleton
    fun provideSavedPostsDataSource(
        db: BloggerDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): SavedPostsDataSource {
        return SavedPostsDataSourceImpl(
            db = db,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideCachedPostsDataSource(
        db: BloggerDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CachedPostsDataSource {
        return CachedPostsDataSourceImpl(
            db = db,
            ioDispatcher = ioDispatcher
        )
    }


    @Provides
    @Singleton
    fun providePostRepository(
        savedPostsDataSource: SavedPostsDataSource,
        cachedPostsDataSource: CachedPostsDataSource,
        remotePostsDataSource: RemotePostsDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher

    ): PostRepository {
        return PostRepositoryImpl(
            savedPostsDataSource = savedPostsDataSource,
            cachedPostsDataSource = cachedPostsDataSource,
            remotePostsDataSource = remotePostsDataSource,
            ioDispatcher = ioDispatcher,
        )
    }

    @Provides
    @Singleton
    fun provideDraftRepository(db: BloggerDatabase): DraftRepository {
        return DraftRepositoryImpl(db = db)
    }

    @Provides
    @Singleton
    fun provideNetworkInfoRepository(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): NetworkInfoRepository {
        return NetworkInfoRepositoryImpl(context = context, ioDispatcher = ioDispatcher)
    }

}