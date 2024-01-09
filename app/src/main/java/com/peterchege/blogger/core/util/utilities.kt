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


import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(dateStr: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val dateTime = LocalDateTime.parse(dateStr, formatter)

    val formatted = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(dateTime)
    return formatted
}

// date string by default in prisma are in UTC
// we need to support different time zones
@RequiresApi(Build.VERSION_CODES.O)
fun addThreeHoursToDateString(dateString: String): String {
    // Define the date format of the input string
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    // Parse the input string to LocalDateTime
    val dateTime = LocalDateTime.parse(dateString, formatter)

    // Add 3 hours to the date
    val updatedDateTime = dateTime.plusHours(3)

    // Format the updated date to the same string format
    val updatedDateString = updatedDateTime.format(formatter)

    return updatedDateString
}