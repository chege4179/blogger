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