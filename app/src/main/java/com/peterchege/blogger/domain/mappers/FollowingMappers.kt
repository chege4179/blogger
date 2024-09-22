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
package com.peterchege.blogger.domain.mappers

import com.peterchege.blogger.core.api.responses.models.FollowerUser
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.room.entities.FollowingUserEntity


fun User.toFollowingEntity(): FollowingUserEntity {
    return FollowingUserEntity(
        userId = userId,
        email = email,
        fullName = fullName,
        imageUrl = imageUrl,
        username = username,
        createdAt = createdAt,
        updatedAt= updatedAt,
        _count = count
    )
}

fun FollowerUser.toFollowingEntity(): FollowingUserEntity {
    return FollowingUserEntity(
        userId = userId,
        email = email,
        fullName = fullName,
        imageUrl = imageUrl,

        username = username,
        createdAt = createdAt,
        updatedAt= updatedAt,
        _count = count
    )
}
fun FollowingUserEntity.toFollower(): FollowerUser {
    return FollowerUser(
        userId = userId,
        email = email,
        fullName = fullName,
        imageUrl = imageUrl,
        username = username,
        createdAt = createdAt,
        updatedAt= updatedAt,
        count = _count
    )
}

fun FollowingUserEntity.toUser(): User {
    return User(
        userId = userId,
        email = email,
        fullName = fullName,
        imageUrl = imageUrl,
        username = username,
        createdAt = createdAt,
        updatedAt= updatedAt,
        count = _count,
        deviceTokens = emptyList(),
        isEmailVerified = true
    )
}