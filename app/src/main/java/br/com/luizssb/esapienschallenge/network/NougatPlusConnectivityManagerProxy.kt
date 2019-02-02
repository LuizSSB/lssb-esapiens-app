package br.com.luizssb.esapienschallenge.network

import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.support.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class NougatPlusConnectivityManagerProxy(
    private val connectivityManager: ConnectivityManager
) : ConnectivityManagerProxy {
    override val isConnected
        get() = connectivityManager.activeNetworkInfo.isConnected

    private val registeredCallbacks =
        HashMap<(Boolean) -> Unit, ConnectivityManager.NetworkCallback>()

    override fun registerNetworkStatusChangeCallback(
        connectionStatusChangeHandler: (connected: Boolean) -> Unit
    ) {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                super.onAvailable(network)
                connectionStatusChangeHandler(true)
            }

            override fun onLost(network: Network?) {
                super.onLost(network)
                connectionStatusChangeHandler(false)
            }
        }

        registeredCallbacks[connectionStatusChangeHandler] = callback
        connectivityManager.registerDefaultNetworkCallback(callback)
    }

    override fun unregisterNetworkStatusChangeCallback(
        connectionStatusChangeHandler: (connected: Boolean) -> Unit
    ) {
        val callback = registeredCallbacks.remove(connectionStatusChangeHandler)
        if (callback != null) {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}