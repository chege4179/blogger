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
package com.peterchege.blogger.core.api.responses.models

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val notificationId:String,
    val notificationType:String,

    val senderId:String,
    val recieverId:String,

    val notificationContent:String,

    val createdAt:String,
    val updatedAt:String,

    val notificationSender: PostAuthor,
    val notificationReceiver: PostAuthor,

    val notificationPostId:String?,
    val notificationCommentId:String?
)