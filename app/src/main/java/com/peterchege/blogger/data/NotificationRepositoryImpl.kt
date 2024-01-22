package com.peterchege.blogger.data

import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.api.responses.responses.GetUserNotificationsResponse
import com.peterchege.blogger.core.api.safeApiCall
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api:BloggerApi
) :NotificationRepository{

    override suspend fun getUserNotifications(
        userId: String,
        page: Int
    ): NetworkResult<GetUserNotificationsResponse> {
        return safeApiCall{ api.getUserNotifications(userId = userId, page = page) }
    }
}