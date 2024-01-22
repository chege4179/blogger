package com.peterchege.blogger.domain.repository

import com.peterchege.blogger.core.api.responses.responses.GetUserNotificationsResponse
import com.peterchege.blogger.core.util.NetworkResult

interface NotificationRepository {
    suspend fun getUserNotifications(userId:String,page:Int):NetworkResult<GetUserNotificationsResponse>
}