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
        password = password,
        username = username,
        createdAt = createdAt,
        updatedAt= updatedAt,
        _count = _count
    )
}

fun FollowerUser.toFollowingEntity(): FollowingUserEntity {
    return FollowingUserEntity(
        userId = userId,
        email = email,
        fullName = fullName,
        imageUrl = imageUrl,
        password = password,
        username = username,
        createdAt = createdAt,
        updatedAt= updatedAt,
        _count = _count
    )
}
fun FollowingUserEntity.toFollower(): FollowerUser {
    return FollowerUser(
        userId = userId,
        email = email,
        fullName = fullName,
        imageUrl = imageUrl,
        password = password,
        username = username,
        createdAt = createdAt,
        updatedAt= updatedAt,
        _count = _count
    )
}

fun FollowingUserEntity.toUser(): User {
    return User(
        userId = userId,
        email = email,
        fullName = fullName,
        imageUrl = imageUrl,
        password = password,
        username = username,
        createdAt = createdAt,
        updatedAt= updatedAt,
        _count = _count,
        deviceTokens = emptyList()
    )
}