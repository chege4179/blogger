package com.peterchege.blogger.core.api.responses.responses

import kotlinx.serialization.Serializable


@Serializable
data class OtpTriggerResponse(
    val msg:String,
    val success:Boolean
)


@Serializable
data class OtpValidateResponse(
    val msg:String,
    val success:Boolean
)