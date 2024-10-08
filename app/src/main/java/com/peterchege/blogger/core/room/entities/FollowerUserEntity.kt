/*
 * Copyright 2024 Blogger
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
package com.peterchege.blogger.core.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peterchege.blogger.core.api.responses.models.DeviceToken
import com.peterchege.blogger.core.api.responses.models.UserCount

@Entity(tableName = "followers")
data class FollowerUserEntity(
    @PrimaryKey
    val userId: String,
    val email: String,
    val fullName: String,
    val imageUrl: String,
    val username: String,
    val createdAt:String,
    val updatedAt:String,
    val _count: UserCount,
)