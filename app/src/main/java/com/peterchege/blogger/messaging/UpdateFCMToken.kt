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
package com.peterchege.blogger.messaging

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.peterchege.blogger.api.BloggerApi
import com.peterchege.blogger.api.requests.UpdateToken
import com.peterchege.blogger.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

//class UpdateFCMToken @Inject constructor(
//    private val api: BloggerApi,
//    private val sharedPreferences: SharedPreferences,
//
//):Worker() {
//    override  fun doWork(): Result {
//        updateToken()
//    }
//    suspend fun updateToken(newToken: String) {
//        val oldToken = sharedPreferences.getString(Constants.FCM_TOKEN,null)
//        sharedPreferences.edit().remove(Constants.FCM_TOKEN).commit()
//        val sharedPrefEditor = sharedPreferences.edit()
//        sharedPrefEditor.apply {
//            putString(Constants.FCM_TOKEN,newToken)
//            apply()
//        }
//        val userId = sharedPreferences.getString(Constants.LOGIN_ID,null)
//        try {
//            val updateTokenResponse = api.updateToken(
//                UpdateToken(
//                    oldToken = oldToken!!,
//                    newToken = newToken,
//                    userId = userId!!
//                )
//            )
//
//        }catch (e: HttpException){
//
//        }catch (e: IOException){
//
//        }
//
//
//    }
//
//
//}
//class UploadWorker(appContext: Context, workerParams: WorkerParameters):
//    Worker(appContext, workerParams) {
//    val updateFCMToken:UpdateFCMToken
//    override fun doWork(): Result {
//
//
//
//
//        // Indicate whether the work finished successfully with the Result
//        return Result.success()
//    }
//}