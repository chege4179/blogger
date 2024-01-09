package com.peterchege.blogger.domain.mappers

import com.peterchege.blogger.core.api.responses.models.FollowerUser
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.room.entities.FollowerUserEntity


fun FollowerUser.toUser(): User {
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

fun User.toFollower(): FollowerUser {
    return FollowerUser(
        userId = userId,
        email = email,
        fullName = fullName,
        imageUrl = imageUrl,
        password = password,
        username = username,
        createdAt = createdAt,
        updatedAt= updatedAt,
        _count = _count,
    )
}

fun User.toFollowerEntity(): FollowerUserEntity {
    return FollowerUserEntity(
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

fun FollowerUser.toFollowerEntity(): FollowerUserEntity {
    return FollowerUserEntity(
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
fun FollowerUserEntity.toFollower(): FollowerUser {
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

fun FollowerUserEntity.toUser(): User {
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