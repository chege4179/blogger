package com.peterchege.blogger.core.api.requests

import kotlinx.serialization.Serializable


@Serializable
data class OtpTriggerBody(
    val email:String
)