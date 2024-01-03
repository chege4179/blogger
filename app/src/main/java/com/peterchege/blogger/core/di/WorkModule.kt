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
import com.peterchege.blogger.core.work.sync_user_data.SyncUserDataWorkManager
import com.peterchege.blogger.core.work.sync_user_data.SyncUserDataWorkManagerImpl
import com.peterchege.blogger.core.work.upload_post.UploadPostWorkManager
import com.peterchege.blogger.core.work.upload_post.UploadPostWorkManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object WorkModule {


    @Provides
    @Singleton
    fun provideUploadPostWorkManager(
        @ApplicationContext context: Context,
    ): UploadPostWorkManager {
        return UploadPostWorkManagerImpl(context = context)

    }

    @Provides
    @Singleton
    fun provideSyncUserDataWorkManager(
    ): SyncUserDataWorkManager {
        return SyncUserDataWorkManagerImpl()

    }
}
