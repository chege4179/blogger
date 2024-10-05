package com.peterchege.blogger.domain.repository

import com.peterchege.blogger.core.api.requests.CaptureDeviceInfoDto
import com.peterchege.blogger.core.api.responses.responses.CaptureDeviceInfoResponse
import com.peterchege.blogger.core.util.NetworkResult

interface DeviceInfoRepository {

    suspend fun captureDeviceInfo(payload:CaptureDeviceInfoDto):NetworkResult<CaptureDeviceInfoResponse>
}