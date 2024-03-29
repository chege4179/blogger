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


import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.text.format.DateFormat
import android.widget.Toast
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.google.android.play.core.review.ReviewManagerFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}


fun truncateString(str: String, n: Int): String {
    return if (str.length > n) {
        "${str.substring(0, n - 1)}...."
    } else {
        str
    }
}

fun showReviewDialog(activity: Activity, onComplete: () -> Unit, onFailure: () -> Unit) {
    val reviewManager = ReviewManagerFactory.create(activity.applicationContext)
    reviewManager.requestReviewFlow()
        .addOnCompleteListener {
            if (it.isSuccessful) {
                reviewManager.launchReviewFlow(activity, it.result)
                    .addOnSuccessListener {
                        onComplete()
                    }
                    .addOnFailureListener {
                        onFailure()
                    }
            }else{
                onFailure()
            }
        }
        .addOnFailureListener {
            it.printStackTrace()
            onFailure()

        }
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.setTagAndId(tag: String): Modifier {
    return this
        .semantics { testTagsAsResourceId = true }
        .testTag(tag)
}

fun convertUtcDateStringToReadable(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat =
        DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyy-MM-dd h:mm:ss a ")

    val utcDate: Date
    try {
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        utcDate = inputFormat.parse(dateString)!!
    } catch (e: ParseException) {
        e.printStackTrace()
        return "Error parsing date"
    }

    val outputFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
    outputFormatter.timeZone = TimeZone.getDefault()

    return outputFormatter.format(utcDate)
}