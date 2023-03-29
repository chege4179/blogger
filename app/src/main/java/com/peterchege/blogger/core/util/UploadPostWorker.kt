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
package com.peterchege.blogger.core.util

import android.content.Context
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.peterchege.blogger.R
import com.peterchege.blogger.core.api.BloggerApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

@Suppress("BlockingMethodInNonBlockingContext")
class UploadPostWorker @Inject constructor(
    private val context: Context,
    private val workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val uri = inputData.getString("uri")
        val postTitle = inputData.getString("postTitle")
        val postBody = inputData.getString("postBody")
        val postedBy = inputData.getString("postedBy")
        val postedAt = inputData.getString("postedAt")
        val postedOn = inputData.getString("postedOn")
        startForegroundService()


        val file = UriToFile(context = context).getImageBody(Uri.parse(uri))
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder
            .addFormDataPart("postTitle", postTitle!!)
            .addFormDataPart("postBody", postBody!!)
            .addFormDataPart("postedBy", postedBy!!)
            .addFormDataPart("postedAt", postedAt!!)
            .addFormDataPart("postedOn", postedOn!!)
            .addFormDataPart("photo", file.name, requestFile)

        val requestBody: RequestBody = builder.build()

        try {
            val response = BloggerApi.instance.postImage(body = requestBody)
            if (response.success) {
                Result.success(
                    workDataOf(
                        WorkerKeys.MSG to response.msg,
                        WorkerKeys.IS_LOADING to false,
                        WorkerKeys.SUCCESS to response.success

                    )
                )

            }

        } catch (e: HttpException) {

            Result.failure(
                workDataOf(
                    WorkerKeys.MSG to "Could not reach server at the moment",
                    WorkerKeys.IS_LOADING to false,
                    WorkerKeys.SUCCESS to false

                )
            )

        } catch (e: IOException) {

            Result.failure(
                workDataOf(
                    WorkerKeys.MSG to "The server is down ....Please try again later",
                    WorkerKeys.IS_LOADING to false,
                    WorkerKeys.SUCCESS to false

                )
            )

        }
        return Result.failure(
            workDataOf(WorkerKeys.MSG to "Unknown error")
        )


    }

    private suspend fun startForegroundService() {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText("Uploading...")
                    .setContentTitle("Your post is being uploaded")
                    .build()

            )
        )

    }

}