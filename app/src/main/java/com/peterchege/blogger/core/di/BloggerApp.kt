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

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.peterchege.blogger.BuildConfig
import com.peterchege.blogger.core.analytics.crashlytics.CrashlyticsTree
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.ProfileVerifierLogger
import com.peterchege.blogger.core.work.WorkConstants
import com.peterchege.blogger.core.work.WorkInitializer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class BloggerApp :Application(){

//    @Inject
//    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var profileVerifierLogger: ProfileVerifierLogger

    override fun onCreate() {
        super.onCreate()
        initTimber()
        setUpNotificationChannel()
        profileVerifierLogger()


    }


    private fun initTimber() = when {
        BuildConfig.DEBUG -> {
            Timber.plant(Timber.DebugTree())
        }
        else -> {
            Timber.plant(CrashlyticsTree())
        }
    }

    private fun setUpNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL,
                WorkConstants.uploadPostWorkerName,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}