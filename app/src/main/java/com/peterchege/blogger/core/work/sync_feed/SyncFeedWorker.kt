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
package com.peterchege.blogger.core.work.sync_feed

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.peterchege.blogger.R
import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.data.local.posts.cache.CachedPostsDataSource
import com.peterchege.blogger.data.remote.posts.RemotePostsDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.random.Random


@HiltWorker
class SyncFeedWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val remotePostsDataSource: RemotePostsDataSource,
    private val cachedPostsDataSource: CachedPostsDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) :CoroutineWorker(context,workerParams){

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            Random.nextInt(),
            NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.blogger_app_icon)
                .setContentTitle("Syncing")
                .build()

        )
    }


    override suspend fun doWork(): Result {
        startForegroundService(notificationInfo = "Syncing Feed .......")
        val remotePosts = remotePostsDataSource.getAllPosts()
        when(remotePosts){
            is NetworkResult.Success -> {
                cachedPostsDataSource.deleteAllPostsFromCache()
                cachedPostsDataSource.insertCachedPosts(posts = remotePosts.data.posts)
                return Result.success()
            }
            is NetworkResult.Error -> {
                startForegroundService(notificationInfo = "Failed to sync feed")
                Result.retry()
                return Result.failure()
            }
            is NetworkResult.Exception -> {
                startForegroundService(notificationInfo = "Failed to sync feed")
                return Result.failure()
            }
        }
    }

    private suspend fun startForegroundService(notificationInfo:String) {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(notificationInfo)
                    .build()

            )
        )

    }


}