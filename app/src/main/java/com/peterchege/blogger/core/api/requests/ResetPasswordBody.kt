package com.peterchege.blogger.core.api.requests

import kotlinx.serialization.Serializable


@Serializable
data class ResetPasswordBody(
    val email:String,
    val newPassword:String,
)