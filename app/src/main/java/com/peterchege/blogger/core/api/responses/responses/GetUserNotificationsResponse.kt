package com.peterchege.blogger.core.api.responses.responses

import com.peterchege.blogger.core.api.responses.models.Notification
import kotlinx.serialization.Serializable

@Serializable
data class GetUserNotificationsResponse(
    val msg:String,
    val success:Boolean,
    val nextPage:Int?,
    val notificationsCount:Int?,
    val notifications:List<Notification>?,
)