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
import android.net.Uri
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.peterchege.blogger.core.api.requests.PostBody
import com.peterchege.blogger.core.work.WorkConstants
import com.peterchege.blogger.core.work.anyRunning
import com.peterchege.blogger.core.work.upload_post.UploadPostWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import timber.log.Timber
import javax.inject.Inject

interface SyncFeedWorkManager {
    val isSyncing: Flow<Boolean>

    suspend fun startSync()


}

class SyncFeedWorkManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
):SyncFeedWorkManager {

    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(
        WorkConstants.syncFeedWorkName
    )
        .map(MutableList<WorkInfo>::anyRunning)
        .asFlow()
        .conflate()

    override suspend fun startSync() {
        Timber.d("Starting normal sync")
        val syncFeedRequest = OneTimeWorkRequestBuilder<SyncFeedWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(
                        NetworkType.CONNECTED
                    )
                    .build()
            )
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.beginUniqueWork(
            WorkConstants.syncFeedWorkName,
            ExistingWorkPolicy.KEEP,
            syncFeedRequest
        )
            .enqueue()
    }



}