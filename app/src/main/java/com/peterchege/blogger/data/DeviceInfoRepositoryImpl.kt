package com.peterchege.blogger.data

import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.api.requests.CaptureDeviceInfoDto
import com.peterchege.blogger.core.api.responses.responses.CaptureDeviceInfoResponse
import com.peterchege.blogger.core.api.safeApiCall
import com.peterchege.blogger.core.util.NetworkResult
import com.peterchege.blogger.domain.repository.DeviceInfoRepository
import javax.inject.Inject

class DeviceInfoRepositoryImpl @Inject constructor(
    private val api: BloggerApi
):DeviceInfoRepository {


    override suspend fun captureDeviceInfo(payload: CaptureDeviceInfoDto): NetworkResult<CaptureDeviceInfoResponse> {
        return safeApiCall { api.captureDeviceDetails(payload) }
    }
}