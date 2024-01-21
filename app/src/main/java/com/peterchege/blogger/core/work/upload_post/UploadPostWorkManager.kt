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
package com.peterchege.blogger.core.work.upload_post

import android.content.Context
import android.net.Uri
import androidx.lifecycle.map
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.asFlow
import androidx.work.ExistingWorkPolicy
import androidx.work.workDataOf
import com.peterchege.blogger.core.api.requests.PostBody
import com.peterchege.blogger.core.work.WorkConstants.uploadPostWorkerName
import com.peterchege.blogger.core.work.anyRunning
import kotlinx.coroutines.flow.conflate
import javax.inject.Inject

interface UploadPostWorkManager {
    val isUploading: Flow<Boolean>

    suspend fun startUpload(postBody:PostBody,uri: Uri)
}

class UploadPostWorkManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
): UploadPostWorkManager {



    override val isUploading: Flow<Boolean> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(uploadPostWorkerName)
        .map(MutableList<WorkInfo>::anyRunning)
        .asFlow()
        .conflate()

    override suspend fun startUpload(postBody: PostBody,uri: Uri) {
        val postArticleParams = workDataOf(
            "uri" to uri.toString(),
            "postBody" to postBody.postBody,
            "postTitle" to postBody.postTitle,
            "photo" to postBody.photo,
            "userId" to postBody.userId
        )
        val postArticleRequest = OneTimeWorkRequestBuilder<UploadPostWorker>()
            .setInputData(postArticleParams)
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
                uploadPostWorkerName,
                ExistingWorkPolicy.KEEP,
                postArticleRequest
            )
            .enqueue()
    }
}


