package com.peterchege.blogger.core.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class PostAuthor(
    val userId: String,
    val email: String,
    val username: String,
    val password: String,
    val fullName: String,
    val imageUrl: String,
    val createdAt: String,
    val updatedAt: String,
)