package br.com.luizssb.esapienschallenge.network

// Luiz: do nothing. Null pattern.
class NullConnectivityManagerProxy : ConnectivityManagerProxy {
    override fun unregisterNetworkStatusChangeCallback(
        connectionStatusChangeHandler: (connected: Boolean) -> Unit
    ) = Unit

    override fun registerNetworkStatusChangeCallback(
        connectionStatusChangeHandler: (connected: Boolean) -> Unit
    ) = Unit
}