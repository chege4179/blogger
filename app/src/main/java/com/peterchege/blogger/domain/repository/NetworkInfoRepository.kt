package com.peterchege.blogger.domain.repository

import kotlinx.coroutines.flow.Flow


sealed class NetworkStatus {
    object Unknown: NetworkStatus()
    object Connected: NetworkStatus()
    object Disconnected: NetworkStatus()
}
interface NetworkInfoRepository {

    val networkStatus: Flow<NetworkStatus>
}