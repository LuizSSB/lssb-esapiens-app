package br.com.luizssb.esapienschallenge.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class ReachabilityReceiver(private val connectivityManager: ConnectivityManager)
    : BroadcastReceiver(), ConnectivityManagerProxy {
    private val observers = HashSet<(Boolean) -> Unit>()

    override fun registerNetworkStatusChangeCallback(
        connectionStatusChangeHandler: (connected: Boolean) -> Unit
    ) {
        if (!observers.contains(connectionStatusChangeHandler)) {
            observers.add(connectionStatusChangeHandler)
        }
    }

    override fun unregisterNetworkStatusChangeCallback(
        connectionStatusChangeHandler: (connected: Boolean) -> Unit
    ) {
        observers.remove(connectionStatusChangeHandler)
    }

    override val isConnected: Boolean
        get() = connectivityManager.activeNetworkInfo.isConnected

    override fun onReceive(context: Context, intent: Intent) {
        val isNetworkAvailable = isConnected

        for(handler in observers) {
            handler(isNetworkAvailable)
        }
    }
}
