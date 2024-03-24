package com.peterchege.blogger.core.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class ValidateOtpBody(
    val email:String,
    val otpPassword:String,
)