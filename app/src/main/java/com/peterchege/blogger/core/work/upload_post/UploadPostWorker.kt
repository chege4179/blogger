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
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.peterchege.blogger.R
import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.UriToFile
import com.peterchege.blogger.core.util.WorkerKeys
import com.peterchege.blogger.domain.repository.PostRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random


@HiltWorker
class UploadPostWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val postRepository: PostRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val uri = inputData.getString("uri")
        val postTitle = inputData.getString("postTitle")
        val postBody = inputData.getString("postBody")
        val userId = inputData.getString("userId")


        startForegroundService(
            notificationTitle = "Uploading Post",
            notificationInfo = "Your post is being uploaded"
        )
        val file = UriToFile(context = context).getImageBody(Uri.parse(uri))
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder
            .addFormDataPart("postTitle", postTitle!!)
            .addFormDataPart("postBody", postBody!!)
            .addFormDataPart("userId", userId!!)
            .addFormDataPart("photo", file.name, requestFile)

        val requestBody: RequestBody = builder.build()

        val response = postRepository.uploadPost(body = requestBody)
        when(response){
            is NetworkResult.Success -> {
                if (response.data.success){
                    startForegroundService(
                        notificationTitle = "Post uploaded successfully",
                        notificationInfo = "Your post has been uploaded successfully"
                    )
                    return Result.success(
                        workDataOf(
                            WorkerKeys.MSG to response.data.msg,
                            WorkerKeys.IS_LOADING to false,
                            WorkerKeys.SUCCESS to response.data.success
                        )
                    )
                }

            }
            is NetworkResult.Error -> {
                startForegroundService(
                    notificationTitle = "Upload Failed !!",
                    notificationInfo = "Could not reach server at the moment"
                )
                return Result.failure(
                    workDataOf(
                        WorkerKeys.MSG to "Could not reach server at the moment",
                        WorkerKeys.IS_LOADING to false,
                        WorkerKeys.SUCCESS to false

                    )
                )
            }
            is NetworkResult.Exception -> {
                startForegroundService(
                    notificationTitle = "Upload Failed !!",
                    notificationInfo = "The server is down ....Please try again later"
                )
                return Result.failure(
                    workDataOf(
                        WorkerKeys.MSG to "The server is down ....Please try again later",
                        WorkerKeys.IS_LOADING to false,
                        WorkerKeys.SUCCESS to false
                    )
                )
            }
        }
        startForegroundService(
            notificationTitle = "Upload Failed !!",
            notificationInfo = "Unknown error"
        )
        return Result.failure(
            workDataOf(
                WorkerKeys.MSG to "The server is down ....Please try again later",
                WorkerKeys.IS_LOADING to false,
                WorkerKeys.SUCCESS to false
            )
        )



    }

    private suspend fun startForegroundService(notificationTitle:String, notificationInfo:String) {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText(notificationTitle)
                    .setContentTitle(notificationInfo)
                    .build()

            )
        )

    }




}

