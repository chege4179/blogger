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
package com.peterchege.blogger.core.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

//class UploadPostWorkerFactory @Inject constructor(
//    val workerFactories: Map<Class<out CoroutineWorker>,
//            @JvmSuppressWildcards Provider<ChildWorkerFactory>>
//) : WorkerFactory() {
//
//    override fun createWorker(
//        appContext: Context,
//        workerClassName: String,
//        workerParameters: WorkerParameters
//    ): ListenableWorker? {
//        val foundEntry =
//            workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
//        val factoryProvider = foundEntry?.value
//            ?: throw IllegalArgumentException("unknown worker class name: $workerClassName")
//        return factoryProvider.get().create(appContext, workerParameters)
//    }
//}