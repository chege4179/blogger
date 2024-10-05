package com.peterchege.blogger.core.api.responses.responses

import kotlinx.serialization.Serializable


@Serializable
data class CaptureDeviceInfoResponse(
    val msg: String,
    val success: Boolean,
)