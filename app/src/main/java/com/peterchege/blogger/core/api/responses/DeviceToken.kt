package com.peterchege.blogger.core.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class DeviceToken(
    val deviceId:String,
    val deviceToken:String,
    val deviceTokenUserId:String,
)