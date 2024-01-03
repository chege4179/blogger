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
package com.peterchege.blogger.core.api.requests

import com.peterchege.blogger.core.api.responses.User
import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val notificationType:String,
    val notificationSender:User,
    val notificationReceiver: User,
    val senderId:String,
    val receiverId:String,
    val notificationContent:String,
    val createdAt:String? = null,
    val updatedAt:String? = null,
)