package com.peterchege.blogger.core.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.peterchege.blogger.core.work.sync_feed.SyncFeedWorker

object WorkInitializer {
    fun initialize(context: Context) {
        val request = OneTimeWorkRequestBuilder<SyncFeedWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(
                Constraints.Builder()
                .setRequiredNetworkType(
                    NetworkType.CONNECTED
                )
                .build())
            .build()
        WorkManager.getInstance(context).apply {
            enqueueUniqueWork(WorkConstants.syncFeedWorkName,
                ExistingWorkPolicy.KEEP,request)
        }
    }
}