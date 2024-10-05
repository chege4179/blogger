package com.peterchege.blogger.core.api.requests

import kotlinx.serialization.Serializable


@Serializable
data class CaptureDeviceInfoDto(
    val deviceId: String,
    val deviceName: String,
    val deviceType: String,
    val osVersion: String,
    val platform: String,
    val apkVersion: String,
    val fcmToken: String,
    val deviceTokenUserId: String
)
