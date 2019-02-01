package br.com.luizssb.esapienschallenge.network

interface ConnectivityManagerProxy {
    fun registerNetworkStatusChangeCallback(
        connectionStatusChangeHandler:(connected: Boolean) -> Unit
    )
    fun unregisterNetworkStatusChangeCallback(
        connectionStatusChangeHandler:(connected: Boolean) -> Unit
    )
}