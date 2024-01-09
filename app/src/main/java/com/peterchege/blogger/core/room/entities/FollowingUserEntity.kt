package com.peterchege.blogger.core.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peterchege.blogger.core.api.responses.models.UserCount

@Entity(tableName = "followings")
data class FollowingUserEntity(
    @PrimaryKey
    val userId: String,
    val email: String,
    val fullName: String,
    val imageUrl: String,
    val password: String,
    val username: String,
    val createdAt:String,
    val updatedAt:String,
    val _count: UserCount,
)