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

object Constants {
    const val LOCAL_URL="http://10.0.2.2:9000"
    const val LOCAL_URL_2 = "http://192.168.1.10:9000"

    const val REMOTE_URL = "https://bloggerserver-production.up.railway.app"
    const val REMOTE_URL_2 = "https://blogger-server-vfua.onrender.com"

    const val BASE_URL = LOCAL_URL_2

    const val FCM_TOKEN ="FCM_TOKEN"

    const val CHANNEL_ID="CHANNEL_ID"

    const val DATABASE_NAME = "blogger_db"

    const val FOLLOWER="FOLLOWER"
    const val FOLLOWING="FOLLOWING"

    const val NOTIFICATION_CHANNEL = "upload_channel"

    const val SEARCH_TYPE_POSTS = "Posts"
    const val SEARCH_TYPE_USERS = "Users"

    const val USER_PREFERENCES = "USER_PREFERENCES"
}