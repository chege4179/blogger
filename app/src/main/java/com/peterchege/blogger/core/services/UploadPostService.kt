/*
 * Copyright 2024 Blogger
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
package com.peterchege.blogger.core.services

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.peterchege.blogger.R
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.core.util.UriToFile
import com.peterchege.blogger.domain.repository.PostRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject


@AndroidEntryPoint
class UploadPostService : Service() {
    @Inject
    lateinit var postRepository: PostRepository
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> {
                val postTitle = intent.getStringExtra("postTitle")
                val postBody = intent.getStringExtra("postBody")
                val uri = intent.getStringExtra("uri")
                val userId = intent.getStringExtra("userId")
                CoroutineScope(Dispatchers.IO).launch {
                    startUploadPost(
                        postTitle = postTitle!!,
                        postBody = postBody!!,
                        uri = uri!!,
                        userId = userId!!
                    )
                }

            }

            Actions.STOP.toString() -> {
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private suspend fun startUploadPost(
        postTitle: String,
        postBody: String,
        uri: String,
        userId: String
    ) {
        startForeground(
            1,
            createNotification(
                notificationTitle = "Uploading....",
                notificationContent = "Post is being uploaded"
            )
        )
        val file = UriToFile(context = this).getImageBody(Uri.parse(uri))
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder
            .addFormDataPart("postTitle", postTitle)
            .addFormDataPart("postBody", postBody)
            .addFormDataPart("userId", userId)
            .addFormDataPart("photo", file.name, requestFile)

        val requestBody: RequestBody = builder.build()

        val response = postRepository.uploadPost(body = requestBody)
        when (response) {
            is NetworkResult.Success -> {
                if (response.data.success) {
                    startForeground(
                        2,
                        createNotification(
                            notificationTitle = "Upload Successful",
                            notificationContent = "Your post was uploaded successfully",
                        )
                    )
                    stopSelf()
                } else {
                    startForeground(
                        3,
                        createNotification(
                            notificationTitle = response.data.msg,
                            notificationContent = response.data.msg
                        )
                    )
                    stopSelf()
                }
            }

            is NetworkResult.Error -> {
                startForeground(
                    4,
                    createNotification(
                        notificationTitle = "A network error occurred",
                        notificationContent = "A network error occurred"
                    )
                )
            }

            is NetworkResult.Exception -> {
                startForeground(
                    5,
                    createNotification(
                        notificationTitle = "A network exception occurred",
                        notificationContent = "A network exception occurred"
                    )
                )
            }
        }
    }

    private fun createNotification(
        notificationTitle: String,
        notificationContent: String
    ): Notification {
        return NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .build()

    }

    enum class Actions {
        START,
        STOP,
    }
}