package com.peterchege.blogger.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.domain.repository.NetworkInfoRepository
import com.peterchege.blogger.domain.repository.NetworkStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NetworkInfoRepositoryImpl @Inject constructor(
    private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,

):NetworkInfoRepository{

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val networkStatus: Flow<NetworkStatus> = callbackFlow {
        val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(element = NetworkStatus.Connected)
            }

            override fun onUnavailable() {
                trySend(element = NetworkStatus.Disconnected)
            }

            override fun onLost(network: Network) {
                trySend(element = NetworkStatus.Disconnected)
            }

        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(request, connectivityCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(connectivityCallback)
        }
    }
        .distinctUntilChanged()
        .flowOn(context = ioDispatcher)
}